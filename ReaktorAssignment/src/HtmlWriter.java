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
    }

    private String wrapWithAHref(String s) {
        return "<a href=\"#" + s + "\">" + s + "</a>";
    }
    
}
