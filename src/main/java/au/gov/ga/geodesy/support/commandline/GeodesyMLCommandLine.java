package au.gov.ga.geodesy.support.commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class GeodesyMLCommandLine {
    private final static Option INPUT_FILE_OPT = Option.builder("in").type(String.class).longOpt("input")
            .argName("file").hasArg().desc("use given buildfile").build();
    private final static Option OUTPUT_DIR_OPT = Option.builder("outdir").type(String.class).longOpt("outdir")
            .argName("dir").hasArg().desc("output directory").build();

    public GeodesyMLCommandLine(String[] args) {
        Options options = new Options();
        options.addOption(INPUT_FILE_OPT);
        options.addOption(OUTPUT_DIR_OPT);

        CommandLineParser parser = new DefaultParser();

        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String inputFile = line.getOptionValue("input");
        String outputDir = line.getOptionValue("outdir");
        System.out.println("  inputFile: " + inputFile);
        System.out.println("  outdir: " + outputDir);
    }
    
    public static void main(String[] args) {
        new GeodesyMLCommandLine(args);
    }
}
