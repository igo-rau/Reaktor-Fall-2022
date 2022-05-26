import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Scanner;

public class Reaktor {

    private static final String sourceFilename = "poetry.lock";
    private static final String targetFilename = "output.html";

    private static String wrapWithAHref(String s) {
        return "<a href=\"#" + s + "\">" + s + "</a>";
    }

    public static void main(String[] args) {

        Packages installedPackages = new Packages();

        // Parsing

        System.out.println("Reading from " + sourceFilename + "...");
        try {
            Scanner fileScanner = new Scanner(Paths.get(sourceFilename));
            PoetryParser poetryParser = new PoetryParser(fileScanner, installedPackages);
            poetryParser.run();
            System.out.println("Parsing complete!");
        } catch (Exception e) {
            System.out.println("Error opening file: " + e.getMessage());
        }

        // At the moment all info is has been read.
        // Next step: generate the reverse dependencies.

        generateRevDependencies(installedPackages);

        // Structure has been built (including reverse dependencies).

        exportToHtml(installedPackages);

    }

    private static void exportToHtml(Packages installedPackages) {

        // Writing to html:
        try {
            FileWriter myWriter = new FileWriter(targetFilename);
            myWriter.write(
                    "<html>\n<head>\n<style>\nhtml {scroll-behavior: smooth !important;}\n</style>\n</head>\n<body style=background-color:beige>\n");
            myWriter.write(
                    "<h1>Reaktor developer's <a href=\"https://www.reaktor.com/assignment-fall-2022-developers/\">assignment</a> - fall 2022</h1>\n");
            myWriter.write("Developed by: Igor Rautiainen <br>\n");
            myWriter.write("Generated: " + new Timestamp(new Date().getTime()) + "<br>\n");
            myWriter.write("<hr>");

            // Div with index
            myWriter.write("<div id=\"top\">");
            myWriter.write("<h2>Installed packages</h2><br>\n");
            for (String packName : installedPackages.getKeySet()) {
                myWriter.write(wrapWithAHref(packName) + ", ");
            }
            //TODO: Fullstop instead of comma at the end.

            myWriter.write("<br>\n<hr>\n</div>\n");

            // Div for each package
            for (String packName : installedPackages.getKeySet()) {
                myWriter.write("<div id=\"" + packName + "\">");
                myWriter.write("<h3 style=display:inline>" + packName + "</h3>" + " (to the "
                        + "<a href=\"#top\">TOP</a>" + ")<br>\n");
                myWriter.write(installedPackages.getPack(packName).getDescription() + "<br>\n");

                String myOutput;

                // Dependencies
                myOutput = "";
                myWriter.write("<h3>Dependencies</h3>\n");
                for (String dependency : installedPackages.getPack(packName).getDependencies()) {
                    if (installedPackages.containsPack(dependency)) {
                        myOutput = myOutput + wrapWithAHref(dependency)+", ";
                    } else {
                        myOutput = myOutput + dependency + ", ";                        
                    }
                }
                if (myOutput.isEmpty()) {
                    myOutput = "No dependencies";
                } else {
                    myOutput = myOutput.substring(0, myOutput.length() - 2) + ".";
                }
                myWriter.write(myOutput);


                // Reversed Dependencies
                myOutput = "";
                myWriter.write("<h3>Reversed dependencies</h3>\n");
                for (String dependency : installedPackages.getPack(packName).getReversedDependencies()) {
                    if (installedPackages.containsPack(dependency)) {
                        myOutput = myOutput + wrapWithAHref(dependency) + ", ";
                    } else {
                        myOutput = myOutput + dependency + ", ";                        
                    }
                }                
                if (myOutput.isEmpty()) {
                    myOutput = "No reversed dependencies";
                } else {
                    myOutput = myOutput.substring(0, myOutput.length() - 2) + ".";
                }
                myWriter.write(myOutput);
                myWriter.write("<br>\n<hr>\n</div>\n");

            }

            // end of file.
            myWriter.write("</body></html>");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void generateRevDependencies(Packages installedPackages) {
        for (Map.Entry<String, Package> m : installedPackages.getEntrySet()) {
            for (String dependency : m.getValue().getDependencies()) {
                if (installedPackages.containsPack(dependency)) {
                    installedPackages.getPack(dependency).addReversedDependency(m.getKey());
                }
            }
        }
    }

}
