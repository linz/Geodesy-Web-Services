package au.gov.ga.geodesy.gws.systemtest;

import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.Test;

import difflib.Delta;
import difflib.Delta.TYPE;
import difflib.DiffRow;
import difflib.DiffRowGenerator;
import difflib.DiffUtils;
import difflib.Patch;

public class Compare extends BaseSystemTest {

    private static final String unifiedDiffs =
        "/home/lbodor/dev/geodesy/GeodesyML-Converter/tests/diffs/";

    private List<String> lines(File file) throws IOException {
        Path filePath = Paths.get(file.getAbsolutePath());
        try {
            return Files.lines(filePath, Charset.forName("utf-8")).collect(Collectors.toList());
        }
        catch (UncheckedIOException e) {
            return Files.lines(filePath, Charset.forName("iso-8859-1")).collect(Collectors.toList());
        }
    }

    public void compare(File a, File b) throws Exception {
        System.out.println("\n***Checking " + a.getName());
        Patch<String> patch = new DiffUtils().diff(a, b);
        analyse(patch);
    }

    @Test
    public void analyseDiffs() throws Exception {
        for (File diff : Paths.get(unifiedDiffs).toFile().listFiles()) {
            List<String> lines = lines(diff);
            if (!lines.isEmpty()) {
                System.out.println("\n***Checking " + diff.getAbsolutePath());
                analyse(DiffUtils.parseUnifiedDiff(lines(diff)));
            }
        }
    }

    private boolean isFieldLine(Pair<String, String> delta) {
        Optional<String> fieldName = split(delta.getLeft()).map(Pair::getLeft);
        return fieldName.isPresent() && !StringUtils.isEmpty(fieldName.get()) && split(delta.getRight()).isPresent();
    }

    private void analyse(Patch<String> patch) {
        Map<Boolean, List<Delta<String>>> deltas = patch.getDeltas().stream()
            .collect(Collectors.partitioningBy(delta -> isSingleLineDelta(delta)));

        Map<Boolean, List<Pair<String, String>>> fieldDeltas = deltas.get(true).stream()
            .map(delta -> Pair.of( 
                 delta.getOriginal().getLines().get(0).trim(),
                 delta.getRevised().getLines().get(0).trim())
            )
            .filter(delta -> !isFtpChange(delta))
            .filter(delta -> !isWhiteSpaceChange(delta))
            .collect(Collectors.partitioningBy(delta -> isFieldLine(delta)));


        fieldDeltas.get(true).stream()
            .filter(delta -> {
                Pair<String, String> aField = split(delta.getLeft()).get();
                Pair<String, String> bField = split(delta.getRight()).get();

                return !isInsignificantChange(aField, bField);
            })
            .forEach(delta -> 
                System.out.println("value mismatch, " + delta.getLeft() + " -> " + delta.getRight())
            );

        fieldDeltas.get(false).stream()
            .filter(delta -> {
                if (delta.getLeft().charAt(0) == ':') {
                    return !equalToIgnoringWhiteSpace(delta.getLeft().substring(1)).matches(delta.getRight());
                }
                return true;
            })
            .filter(delta -> !(delta.getLeft().equals("International GPS Service") && delta.getRight().equals("International GNSS Service")))
            .filter(delta -> !(delta.getLeft().equals("Approximate Position (GDA94)") && delta.getRight().equals("Approximate Position (ITRF)")))
            .filter(delta -> !(delta.getLeft().equals("Approximate Position (GDA)") && delta.getRight().equals("Approximate Position (ITRF)")))
            .forEach(delta ->
                System.out.println("line mismatch, " + delta.getLeft() + " -> " + delta.getRight())
            );

        deltas.get(false).stream()
            .forEach(delta -> {
                if (TYPE.DELETE.equals(delta.getType())) {
                    String deletion = StringUtils.join(delta.getOriginal().getLines(), "\n");
                    System.out.println("Deleted***");
                    System.out.println(deletion);
                } else if (TYPE.INSERT.equals(delta.getType())) {
                    String insertion = StringUtils.join(delta.getRevised().getLines(), "\n");
                    System.out.println("Inserted***");
                    System.out.println(insertion);
                } else {
                    DiffRowGenerator diffRowGenerator = new DiffRowGenerator.Builder()
                        .ignoreWhiteSpaces(true)
                        .build();

                    List<DiffRow> diffRows = diffRowGenerator.generateDiffRows(delta.getOriginal().getLines(), delta.getRevised().getLines());

                    List<DiffRow> output = diffRows.stream()
                        .filter(diffRow -> !equalToIgnoringWhiteSpace(diffRow.getOldLine()).matches(diffRow.getNewLine()))
                        .filter(diffRow ->
                            split(diffRow.getOldLine()).flatMap(aField ->
                            split(diffRow.getNewLine()).map(bField -> {
                                return !isInsignificantChange(aField, bField);
                            })).orElse(true)
                        )
                        .collect(Collectors.toList());

                    boolean emptyDiff = true;
                    for (DiffRow diffRow : output) {
                        Optional<Pair<String, String>> aField = split(diffRow.getOldLine());
                        if (aField.isPresent()) {
                            String aFieldValue = aField.get().getRight();
                            // System.out.println(aFieldValue + " is empty " + isEmpty(aFieldValue));
                            // System.out.println(diffRow.getNewLine() + " is empty " + isEmpty(aFieldValue));
                            if (!isEmpty(aFieldValue) || !isEmpty(diffRow.getNewLine())) {
                                emptyDiff = false;
                                break;
                            }
                        }
                    }
                    if (emptyDiff) {
                        return;
                    }

                    if (!output.isEmpty()) {
                        System.out.println("** multiline change **");
                        output.forEach(diffRow ->
                            System.out.println(diffRow.getOldLine() + " -> " + diffRow.getNewLine())
                        );
                    }
                }
            });
    }

    private boolean isInsignificantChange(Pair<String, String> aField, Pair<String, String> bField) {

        if ("APREF DOMES Number".equals(aField.getLeft().trim()) && "IERS DOMES Number".equals(bField.getLeft().trim()) &&
            aField.getRight().equals(bField.getRight())) {

            return true;
        }

        if (equalToIgnoringWhiteSpace(bField.getLeft()).matches(aField.getLeft())) {

            if (isEmpty(aField.getRight()) && isEmpty(bField.getRight())) {
                return true;
            }

            if ("CCYY-MM-DDThh:mmZ".equals(aField.getRight()) && "(CCYY-MM-DDThh:mmZ)".equals(bField.getRight())) {
                return true;
            }

            if ("Previous Site Log".equals(bField.getLeft())) {
                return true;
            }

            if ("Modified/Added Sections".equals(bField.getLeft())) {
                return true;
            }

            if ("Preferred Abbreviation".equals(aField.getLeft()) && StringUtils.isEmpty(bField.getRight())) {
                return true;
            }

            Optional<Pair<String, Optional<Integer>>> numericField = getNumericField(bField);
            if (numericField.isPresent()) {
                if (numericFieldsEquals(aField, bField, numericField.get().getRight())) {
                    return true;
                }
            }

            if ("Temperature Stabiliz.".equals(bField.getLeft())) {
                boolean aIsEmpty = StringUtils.isEmpty(aField.getRight()) || "(none or tolerance in degrees C)".equals(aField.getRight());
                boolean bIsEmpty = "none".equals(bField.getRight());
                return aIsEmpty && bIsEmpty;
            }

            if ("Date Measured".equals(bField.getLeft()) || "Date Installed".equals(bField.getLeft()) || "Date Removed".equals(bField.getLeft())) {
                if (aField.getRight().indexOf('T') == -1) {
                    String[] dateAndTime = bField.getRight().split("T");
                    if (dateAndTime.length == 2 && dateAndTime[1].equals("00:00Z")) {
                        return true;
                    }
                }
            }

            if ("Effective Dates".equals(aField.getLeft())) {
                List<Delta<String>> ds = DiffUtils.diff(Arrays.asList(aField.getRight().split("/")), Arrays.asList(bField.getRight().split("/"))).getDeltas();

                for (Delta<String> d : ds) {
                    if (d.getOriginal().getLines().size() == 1 && d.getRevised().getLines().size() == 1) {
                        String aDate = d.getOriginal().getLines().get(0);
                        String bDate = d.getRevised().getLines().get(0);

                        if ("CCYY-MM-DD".equals(aDate) && "(CCYY-MM-DD)".equals(bDate)) {
                            continue;
                        }
                    }
                    return false;
                };
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(String s) {
        return StringUtils.isEmpty(s) || "()".equals(String.valueOf(s.charAt(0)) + String.valueOf(s.charAt(s.length() - 1)));
    }

    private boolean isSingleLineDelta(Delta<String> delta) {
        return delta.getOriginal().getLines().size() == 1 && delta.getRevised().size() == 1;
    }
    
    private boolean isWhiteSpaceChange(Pair<String, String> lineDelta) {
         return equalToIgnoringWhiteSpace(lineDelta.getLeft()).matches(lineDelta.getRight());
    }

    private boolean isFtpChange(Pair<String, String> lineDelta) {
        return "ftp://igscb.jpl.nasa.gov/pub/station/general/sitelog_instr.txt".equals(lineDelta.getLeft()) &&
               "ftp://igs.org/pub/station/general/sitelog_instr.txt".equals(lineDelta.getRight());
    }

    private boolean isSingleLineField(String field) {
        return !isMultilineField(field);
    }

    private boolean isMultilineField(String field) {
        List<String> multilineFields = Arrays.asList(
            "Additional Information",
            "Notes"
        );
        return multilineFields.contains(field);
    }

    private List<Pair<String, Optional<Integer>>> numericFields() {
        List<Pair<String, Optional<Integer>>> fields = new ArrayList<>();
        fields.add(Pair.of("Antenna Cable Length", Optional.empty()));
        fields.add(Pair.of("Accuracy (% rel h)", Optional.of(2)));
        fields.add(Pair.of("Accuracy", Optional.of(2)));
        fields.add(Pair.of("Accuracy (mm)", Optional.empty()));
        fields.add(Pair.of("Foundation Depth", Optional.empty()));
        fields.add(Pair.of("Height Diff to Ant", Optional.empty()));
        fields.add(Pair.of("Elevation (m,ellips.)", Optional.of(1)));
        fields.add(Pair.of("Alignment from True N", Optional.of(0)));
        fields.add(Pair.of("X coordinate (m)", Optional.of(5)));
        fields.add(Pair.of("Y coordinate (m)", Optional.of(5)));
        fields.add(Pair.of("Z coordinate (m)", Optional.of(5)));
        fields.add(Pair.of("Elevation Cutoff Setting", Optional.of(0)));
        fields.add(Pair.of("Height of the Monument", Optional.empty()));
        fields.add(Pair.of("Height Diff to Ant", Optional.empty()));
        fields.add(Pair.of("Input Frequency", Optional.empty()));
        fields.add(Pair.of("Temperature Stabiliz.", Optional.empty()));
        fields.add(Pair.of("Marker->ARP Up Ecc. (m)", Optional.of(4)));
        fields.add(Pair.of("Marker-&gt;ARP Up Ecc. (m)", Optional.of(4)));
        fields.add(Pair.of("Marker-&gt;ARP North Ecc(m)", Optional.of(4)));
        fields.add(Pair.of("Marker-&gt;ARP East Ecc(m)", Optional.of(4)));
        fields.add(Pair.of("Data Sampling Interval", Optional.empty()));
        fields.add(Pair.of("dx (m)", Optional.empty()));
        fields.add(Pair.of("dy (m)", Optional.empty()));
        fields.add(Pair.of("dz (m)", Optional.empty()));
        fields.add(Pair.of("Latitude (N is +)", Optional.of(2)));
        fields.add(Pair.of("Longitude (E is +)", Optional.of(2)));
        return fields;
    }

    private Optional<Pair<String, Optional<Integer>>> getNumericField(Pair<String, String> field) {
        return numericFields().stream()
            .filter(f -> f.getLeft().equals(field.getLeft()))
            .findFirst();
    }

    // private Integer getPrecision(Pair<String, String> field) {
    //     return numericFields().stream()
    //         .filter(f -> f.getLeft().equals(field.getLeft()))
    //         .map(Pair::getRight)
    //         .findFirst()

    //         .anyMatch(f -> f.equals(field.getLeft()));

    private List<String> extractNumbers2(String s) {
        s = s.replaceAll("\\+\\-", "");
        s = s.replaceAll("\\+/\\-", "");
        return Arrays.asList(s.replaceAll("[^\\.\\+\\-0-9]+", " ").split(" "));
    }

    private List<BigDecimal> extractNumbers(String s, boolean exact) {
        s = s.replaceAll("\\+\\-", "");
        s = s.replaceAll("\\+/\\-", "");
        List<String> ns = Arrays.asList(s.replaceAll("[^\\.\\+\\-0-9]+", " ").split(" "));
        return ns.stream().map(a ->  {
            if (exact) {
                return new BigDecimal(a).stripTrailingZeros();
            } else {
                return new BigDecimal(Double.valueOf(a)).stripTrailingZeros();
            }
        }).collect(Collectors.toList());
    }

    private boolean numericFieldsEquals(Pair<String, String> aField, Pair<String, String> bField, Optional<Integer> precision) {
        try {
            List<BigDecimal> as = extractNumbers2(aField.getRight()).stream()
                .map(a -> {
                    int i = a.indexOf('.');
                    // System.out.println("index of dot " + i);
                    // System.out.println(precision.orElse(-1));
                    int p = precision.orElse(i == -1 ? 0 : (a.length() - a.indexOf('.') + 1));
                    // System.out.println(p);
                       
                    // System.out.println("a.scaling " + a + " to " + p);
                    BigDecimal scaled = new BigDecimal(Double.valueOf(a)).setScale(p, RoundingMode.HALF_DOWN);
                    // scaled = scaled.setScale(p, RoundingMode.HALF_DOWN);
                    // System.out.println("a.got " + scaled);
                    return scaled;
                })
                .collect(Collectors.toList());

            List<BigDecimal> bs = extractNumbers2(bField.getRight()).stream()
                .map(b ->
                    precision
                        .map(p -> {
                            // if (b.scale() < p) {
                                // System.out.println("b.scaling " + b);
                                BigDecimal scaled = new BigDecimal(b).setScale(p, RoundingMode.HALF_DOWN);
                                // System.out.println("b.got " + scaled);
                                return scaled;
                            // } else {
                            //     return b;
                            // }
                        })
                        .orElse(new BigDecimal(b).stripTrailingZeros())
                )
                .collect(Collectors.toList());
            
            if (as.size() > 0 && bs.size() > 0) {
                boolean f = as.get(0).compareTo(bs.get(0)) == 0;
                if (!f) {
                    System.out.println("comparing " + bField.getLeft() + ", " + as.get(0) + " and " + bs.get(0) + ": " + f);
                }
                return f;
            }
            return false;

            // return as.equals(bs);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Optional<Pair<String, String>> split(String line) {
        int i = line.indexOf(':');
        if (i == -1) {
            return Optional.empty();
        } else {
            String field = line.substring(0, i).trim();
            String value = line.substring(i + 1).trim();
            return Optional.of(Pair.of(field, value));
        }
    }
}
