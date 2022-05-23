import java.util.HashSet;
import java.util.Set;

public class Package {

    private String name;
    private String description;
    private boolean isOptional;
    private Set<String> dependencies;
    private Set<String> reversedDependencies;
    

    public Package() {
        this.dependencies = new HashSet<>();
        this.reversedDependencies = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Set<String> getReversedDependencies() {
        return reversedDependencies;
    }

    public void setReversedDependencies(Set<String> reversedDependencies) {
        this.reversedDependencies = reversedDependencies;
    }

    public Set<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<String> dependencies) {
        this.dependencies = dependencies;
    }

    public void addDependency(String dependency) {
        this.dependencies.add(dependency);
    }

    public void addReversedDependency(String dependency) {
        this.reversedDependencies.add(dependency);
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Name: " + this.name + "\nDescription: " + this.description + "\nOptional: " + this.isOptional;
    }

    public void printFullPackageInfo() {
        System.out.println(this);
        System.out.println("\nDependencies:");
        if (dependencies.isEmpty()) {
            System.out.println("NONE");
        }
        for (String s: dependencies) {
            System.out.println(s);
        }
        System.out.println("\nReversed Dependencies:");
        if (reversedDependencies.isEmpty()) {
            System.out.println("NONE");
        }
        for (String s: reversedDependencies) {
            System.out.println(s);
        }
    }

}
