import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Reaktor {

    private static final String sourceFilename = "poetry.lock";
    private static final String targetFilename = "output.html";

    public static void main(String[] args) {

        Packages installedPackages = new Packages();
        parseFromPoetry(installedPackages);
        exportToHtml(installedPackages);

    }

    private static void parseFromPoetry(Packages installedPackages) {
        System.out.println("Reading from " + sourceFilename + "...");
        try {
            Scanner fileScanner = new Scanner(Paths.get(sourceFilename));
            PoetryParser poetryParser = new PoetryParser(fileScanner, installedPackages);
            poetryParser.run();
            System.out.println("Parsing complete!");
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }

    private static void exportToHtml(Packages installedPackages) {

        // Writing to html:
        try {
            FileWriter myFileWriter = new FileWriter(targetFilename);
            HtmlWriter htmlWriter = new HtmlWriter(myFileWriter, installedPackages);
            htmlWriter.run();
            myFileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
