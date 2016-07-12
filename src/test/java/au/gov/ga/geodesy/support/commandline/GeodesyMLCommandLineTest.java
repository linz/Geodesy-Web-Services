package au.gov.ga.geodesy.support.commandline;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GeodesyMLCommandLineTest {

    @Test
    public void testgetFileName01() {
        String path = "a/b/c/file.txt";
        assertThat(GeodesyMLCommandLine.getFileName(path), is("file.txt"));
    }

    @Test
    public void testgetFileName02() {
        String path = "file.txt";
        assertThat(GeodesyMLCommandLine.getFileName(path), is("file.txt"));
    }

    private static final String outputDir = "/tmp/geodesymltest/commandlinetest";

    static {
        Path outputpath = Paths.get(outputDir);
        try {
            Files.createDirectories(outputpath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testreturnTestFileStringVer01() throws IOException {
        String pathString = "a/b/c/file.txt";
        String expected = outputDir + "/file.txt";
        assertThat(GeodesyMLCommandLine.returnTestFile(outputDir, pathString).toString().replaceAll("\\+", "/"), is(expected));

    }

    @Test
    public void testreturnTestFilePathVer01() throws IOException {
        String pathString = "a/b/c/file.txt";
        String expected = outputDir + "/file.txt";
        Path pathStringPath = Paths.get(pathString);
        assertThat(GeodesyMLCommandLine.returnTestFile(outputDir, pathStringPath).toString().replaceAll("\\+", "/"), is(expected));
    }

    @Test
    public void testreturnTestFileStringVer02() throws IOException {
        String pathString = "file.txt";
        String expected = outputDir + "/file.txt";
        assertThat(GeodesyMLCommandLine.returnTestFile(outputDir, pathString).toString().replaceAll("\\+", "/"), is(expected));

    }

    @Test
    public void testreturnTestFilePathVer02() throws IOException {
        String pathString = "file.txt";
        String expected = outputDir + "/file.txt";
        Path pathStringPath = Paths.get(pathString);
        assertThat(GeodesyMLCommandLine.returnTestFile(outputDir, pathStringPath).toString().replaceAll("\\+", "/"), is(expected));
    }

}
