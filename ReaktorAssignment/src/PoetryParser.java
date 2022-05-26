import java.util.Map;
import java.util.Map.Entry;
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
            //TODO: Refactor;
            Map.Entry<String, String> newPackLine = mySplit(currentLine);
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
            currentLine = poetryScanner.nextLine();
        }
        // pointing at empty line after package body;

        currentLine = poetryScanner.nextLine();

        if (currentLine.equals("[package.dependencies]")) {
            currentLine = poetryScanner.nextLine();
            while (!currentLine.isEmpty()) {
                // reading package dependencies
                // considering only left part of "=" sign.
                Map.Entry<String, String> newPackLine = mySplit(currentLine);
                newPackage.addDependency(newPackLine.getKey());
                currentLine = poetryScanner.nextLine();
            }
            currentLine = poetryScanner.nextLine();
        }

        if (currentLine.equals("[package.extras]")) {
            currentLine = poetryScanner.nextLine();

            while (!currentLine.isEmpty()) {
                Map.Entry<String, String> newPackLine = mySplit(currentLine);
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
                currentLine = poetryScanner.nextLine();
            }
            currentLine = poetryScanner.nextLine();
        }        

        return currentLine;

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

    private static Map.Entry<String, String> mySplit(String stringToSplit) {
        // Divides String into two Strings - key and value. Divided around " = "
        // Removes quotemarks from value, if any
        String[] splittedString = stringToSplit.split(" = ");
        splittedString[1] = myTrim(splittedString[1], '\"', '\"');
        return Map.entry(splittedString[0], splittedString[1]);

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
