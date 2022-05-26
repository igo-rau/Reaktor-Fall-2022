import java.util.Map;
import java.util.Scanner;

public class PoetryParser {
    private Scanner poetryScanner;
    private Packages packages;

    public PoetryParser(Scanner poetryScanner, Packages packages) {
        this.poetryScanner = poetryScanner;
        this.packages = packages;
    }

    public void run() {
        String currentLine = "";
        if (poetryScanner.hasNextLine()) {
            currentLine = poetryScanner.nextLine(); 
        } 
        while (this.poetryScanner.hasNextLine()) {
            if (currentLine.equals("[[package]]")) {
                Package newPackage = new Package();
                currentLine = readPackageBodyAndReturnNextLine(newPackage);
                packages.addPackage(newPackage.getName(), newPackage);
            } else {
                currentLine = poetryScanner.nextLine();                 
            }
        }
        // At the moment all info is has been read.
        // Next step: generate the reverse dependencies.
        generateRevDependencies(packages);
        // Structure has been built (including reverse dependencies).        
    }

    private String readPackageBodyAndReturnNextLine(Package newPackage) {
        String currentLine = poetryScanner.nextLine();
        while (!currentLine.isEmpty()) {
            parseBodyLine(newPackage, currentLine);
            currentLine = poetryScanner.nextLine();
        }
        // pointing at empty line after package body;

        currentLine = poetryScanner.nextLine();
        if (currentLine.equals("[package.dependencies]")) {
            currentLine = poetryScanner.nextLine();
            while (!currentLine.isEmpty()) {
                // reading package dependencies
                // considering only left part of "=" sign.
                parseDependencyLine(newPackage, currentLine);
                currentLine = poetryScanner.nextLine();
            }
            currentLine = poetryScanner.nextLine();
        }

        if (currentLine.equals("[package.extras]")) {
            currentLine = poetryScanner.nextLine();
            while (!currentLine.isEmpty()) {
                parseExtrasLine(newPackage, currentLine);
                currentLine = poetryScanner.nextLine();
            }
            currentLine = poetryScanner.nextLine();
        }        

        return currentLine;

    }

    private void parseExtrasLine(Package newPackage, String currentLine) {
        // Using only right part of " ". removing "[]" from line. Splitting string. Splitting into parts.
        String[] lineValueSplited = myTrim(mySplit(currentLine)[1], '[', ']').split(", ");
        for (String s : lineValueSplited) {
            // Trimming quotes. Only first word remains. 
            newPackage.addDependency(myTrim(s, '\"', '\"').split(" ")[0]);
        }
    }

    private void parseDependencyLine(Package newPackage, String currentLine) {
        String[] newPackLine = mySplit(currentLine);
        newPackage.addDependency(newPackLine[0]);
    }

    private void parseBodyLine(Package newPackage, String currentLine) {
        String[] newPackLine = mySplit(currentLine);
        if (newPackLine[0].equals("name")) {
            newPackage.setName(newPackLine[1]);
        }
        if (newPackLine[0].equals("description")) {
            newPackage.setDescription(newPackLine[1]);
        }
        if (newPackLine[0].equals("optional")) {
            newPackage.setOptional(Boolean.valueOf(newPackLine[1]));
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

    private static String[] mySplit(String stringToSplit) {
        // Divides String into two Strings - key and value. Divided around " = "
        // Removes quotemarks from value, if any
        String[] splittedString = stringToSplit.split(" = ");
        splittedString[1] = myTrim(splittedString[1], '\"', '\"');
        return splittedString;

    }

    private static String myTrim(String stringToTrim, char startSymbol, char endSymbol) {
        // returns trimmed string if symbols can be trimmed, otherwise returns same
        // string;
        if (stringToTrim.charAt(0) == startSymbol && stringToTrim.charAt(stringToTrim.length() - 1) == endSymbol) {
            stringToTrim = stringToTrim.substring(1, stringToTrim.length() - 1);
        }
        return stringToTrim;

    }
}
