import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class Reaktor {

    private static final String sourceFilename = "poetry.lock";
    private static final String targetFilename = "output.html";

    private static String wrapWithAHref(String s) {
        return "<a href=\"#" + s + "\">" + s + "</a>";
    }

    private static String myTrim(String stringToTrim, char startSymbol, char endSymbol) {
        // returns trimmed string if symbols can be trimmed, otherwise returns same
        // string;
        if (stringToTrim.charAt(0) == startSymbol && stringToTrim.charAt(stringToTrim.length() - 1) == endSymbol) {
            stringToTrim = stringToTrim.substring(1, stringToTrim.length() - 1);
        }
        return stringToTrim;

    }

    private static Map.Entry<String, String> mySplit(String stringToSplit) {
        // Divides String into two Strings - key and value. Divided around " = "
        // Removes quotemarks from value, if any
        String[] splittedString = stringToSplit.split(" = ");
        splittedString[1] = myTrim(splittedString[1], '\"', '\"');
        return Map.entry(splittedString[0], splittedString[1]);

    }



    public static void main(String[] args) {

        List<String> poetry = new ArrayList<>();
        SortedMap<String, Package> installedPackages = new TreeMap<>();

        // Reading file into array

        System.out.println("Reading from " + sourceFilename + "...");
        try {
            Scanner fileScanner = new Scanner(Paths.get(sourceFilename));
            while (fileScanner.hasNextLine()) {
                poetry.add(fileScanner.nextLine());
            }
            System.out.println("Reading complete!");
        } catch (Exception e) {
            System.out.println("Error opening file: " + e.getMessage());
        }

        // Parsing Packages;

        parsePackages(poetry, installedPackages);

        // At the moment all info is has been read.
        // Next step: generate the reverse dependencies.

        generateRevDependencies(installedPackages);

        // Structure has been built (including reverse dependencies).

        exportToHtml(installedPackages);

        // test pring of the whole list
        // for (Map.Entry<String, Package> m : installedPackages.entrySet()) {
        // System.out.println();
        // System.out.println("Printing map element: " + m.getKey());
        // m.getValue().printFullPackageInfo();
        // System.out.println("========================");
        // }

    }

    private static void exportToHtml(Map<String, Package> installedPackages) {

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
            for (String packName : installedPackages.keySet()) {
                myWriter.write(wrapWithAHref(packName) + ", ");
            }
            //TODO: Fullstop instead of comma at the end.

            myWriter.write("<br>\n<hr>\n</div>\n");

            // Div for each package
            for (String packName : installedPackages.keySet()) {
                myWriter.write("<div id=\"" + packName + "\">");
                myWriter.write("<h3 style=display:inline>" + packName + "</h3>" + " (to the "
                        + "<a href=\"#top\">TOP</a>" + ")<br>\n");
                myWriter.write(installedPackages.get(packName).getDescription() + "<br>\n");

                String myOutput;

                // Dependencies
                myOutput = "";
                myWriter.write("<h3>Dependencies</h3>\n");
                for (String dependency : installedPackages.get(packName).getDependencies()) {
                    if (installedPackages.containsKey(dependency)) {
                        myOutput = myOutput + wrapWithAHref(dependency)+"</a>, ";
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
                for (String dependency : installedPackages.get(packName).getReversedDependencies()) {
                    if (installedPackages.containsKey(dependency)) {
                        myOutput = myOutput + wrapWithAHref(dependency) + "</a>, ";
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

    private static void generateRevDependencies(Map<String, Package> installedPackages) {
        for (Map.Entry<String, Package> m : installedPackages.entrySet()) {
            for (String dependency : m.getValue().getDependencies()) {
                if (installedPackages.containsKey(dependency)) {
                    installedPackages.get(dependency).addReversedDependency(m.getKey());
                }
            }
        }
    }

    private static void parsePackages(List<String> poetry, Map<String, Package> installedPackages) {
        int counter = 0;
        int poetrySize = poetry.size();

        while (counter <= poetrySize - 1) {

            // Reading package body
            if (poetry.get(counter).equals("[[package]]")) {
                counter++;
                Package newPackage = new Package();

                while (!poetry.get(counter).isEmpty()) {
                    // reading package body
                    Map.Entry<String, String> newPackLine = mySplit(poetry.get(counter));

                    // TwoStrings newPackLine = new TwoStrings(poetry.get(counter));
                    String lineKey = newPackLine.getKey();
                    String lineValue = newPackLine.getValue();
                    if (lineKey.equals("name")) {
                        newPackage.setName(lineValue);
                    }

                    if (lineKey.equals("description")) {
                        newPackage.setDescription(lineValue);
                    }

                    if (lineKey.equals("optional")) {
                        newPackage.setOptional(Boolean.valueOf(lineValue));
                    }

                    counter++;
                }
                // Package body has been read; Counter points the empty string after packege;
                // Checking for package dependencies if any:

                if (poetry.get(counter + 1).equals("[package.dependencies]")) {
                    counter = counter + 2;
                    while (!poetry.get(counter).isEmpty()) {
                        // reading package dependencies
                        // considering only left part of "=" sign.
                        Map.Entry<String, String> newPackLine = mySplit(poetry.get(counter));
                        // TwoStrings newPackLine = new TwoStrings(poetry.get(counter));
                        String lineKey = newPackLine.getKey();
                        newPackage.addDependency(lineKey);
                        counter++;
                    }

                }

                // Package dependencies (if existed) has been read;
                // Counter shows the empty string;
                // Checking for package extras if any;

                if (poetry.get(counter + 1).equals("[package.extras]")) {
                    counter = counter + 2;
                    // reading package extras
                    // considering only right part of "=" sign.
                    while (!poetry.get(counter).isEmpty()) {
                        // TwoStrings newPackLine = new TwoStrings(poetry.get(counter));
                        Map.Entry<String, String> newPackLine = mySplit(poetry.get(counter));
                        String lineValue = newPackLine.getValue();
                        // removing "[]" from line:
                        lineValue = myTrim(lineValue, '[', ']');
                        // Splitting string
                        String[] lineValueSplited = lineValue.split(", ");
                        // removing versions, etc. Only first word remains. :
                        for (int i = 0; i < lineValueSplited.length; i++) {
                            if (lineValueSplited[i].contains(" ")) {
                                lineValueSplited[i] = lineValueSplited[i].substring(1,
                                        lineValueSplited[i].indexOf(" "));
                            } else {
                                lineValueSplited[i] = lineValueSplited[i].substring(1,
                                        lineValueSplited[i].length() - 1);
                            }
                            newPackage.addDependency(lineValueSplited[i]);
                        }

                        counter++;

                    }

                }

                installedPackages.put(newPackage.getName(), newPackage);

                // System.out.println(newPackage);

                // System.out.println(packName+ packVersion+ packDescription+ packCategory+
                // packOptional+ packPythonVersions);
                // packagePool.addPackage(new Package(packName, packVersion, packDescription,
                // packCategory, packOptional, packPythonVersions));
            }
            counter++; // next package

        }
    }

}
