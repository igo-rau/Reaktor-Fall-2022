import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class HtmlWriter {
    private FileWriter myWriter;
    private Packages installedPackages;

    public HtmlWriter(FileWriter myFileWriter, Packages packages) {
        this.myWriter = myFileWriter;
        this.installedPackages = packages;
    }

    public void run() throws IOException {
        writeHeaderDiv();
        writeIndexDiv();
        for (String packName : installedPackages.getKeySet()) {
            writePackageDiv(packName);
        }
        writeEOF();
    }

    private void writeEOF() throws IOException {
        myWriter.write("</body></html>");
    }

    private void writePackageDiv(String packName) throws IOException {
        StringBuilder myOutput = writePackageDivHeader(packName);
        writePackageDivDependencies(packName, myOutput);
        writePackageDivReversedDependecies(packName, myOutput);
        writeEndOfPackageDiv(myOutput);
        myWriter.write(myOutput.toString());
    }

    private void writeEndOfPackageDiv(StringBuilder myOutput) {
        myOutput.append("<br>\n<hr>\n</div>\n");
    }

    private void writePackageDivReversedDependecies(String packName, StringBuilder myOutput) {
        myOutput.append("<h3>Reversed dependencies</h3>\n");
        boolean notFirst = false;
        for (String dependency : installedPackages.getPack(packName).getReversedDependencies()) {
            myOutput.append(notFirst ? ", " : "")
                    .append((installedPackages.containsPack(dependency)) ? wrapWithAHref(dependency) : dependency);
            notFirst = true;
        }
        myOutput.append(
                installedPackages.getPack(packName).getReversedDependencies().isEmpty() ? "No reversed dependencies!"
                        : ".");

    }

    private void writePackageDivDependencies(String packName, StringBuilder myOutput) {
        myOutput.append("<h3>Dependencies</h3>\n");
        boolean notFirst = false;
        for (String dependency : installedPackages.getPack(packName).getDependencies()) {
            myOutput.append(notFirst ? ", " : "")
                    .append((installedPackages.containsPack(dependency)) ? wrapWithAHref(dependency) : dependency);
            notFirst = true;
        }
        myOutput.append(
                installedPackages.getPack(packName).getDependencies().isEmpty() ? "No dependencies!" : ".");
    }

    private StringBuilder writePackageDivHeader(String packName) {
        StringBuilder myOutput = new StringBuilder();
        myOutput.append("<div id=\"" + packName + "\">").append("<h3 style=display:inline>").append(packName)
                .append("</h3> (to the ").append(wrapWithAHref("top")).append(")<br>\n");
        myOutput.append(installedPackages.getPack(packName).getDescription()).append("<br>\n");
        return myOutput;
    }

    private void writeIndexDiv() throws IOException {
        StringBuilder myOutput = new StringBuilder("<div id=\"top\"><h2>Installed packages</h2><br>\n");
        boolean notFirst = false;
        for (String packName : installedPackages.getKeySet()) {
            myOutput.append(notFirst ? ", " : "");
            notFirst = true;
            myOutput.append(wrapWithAHref(packName));
        }
        myOutput.append(".<br>\n<hr>\n</div>\n");
        myWriter.write(myOutput.toString());
    }

    private void writeHeaderDiv() throws IOException {
        StringBuilder myOutput = new StringBuilder(
                "<html>\n<head>\n<style>\nhtml {scroll-behavior: smooth !important;}\n</style>\n</head>\n<body style=background-color:beige>\n<div>");
        myOutput.append(
                "<h1>Reaktor developer's <a href=\"https://www.reaktor.com/assignment-fall-2022-developers/\">assignment</a> - fall 2022</h1>\n");
        myOutput.append("Developed by: Igor Rautiainen <br>\n");
        myOutput.append("Generated: " + new Timestamp(new Date().getTime()) + "<br>\n<hr></div>");
        myWriter.write(myOutput.toString());
    }

    private String wrapWithAHref(String s) {
        return "<a href=\"#" + s + "\">" + s + "</a>";
    }

}
