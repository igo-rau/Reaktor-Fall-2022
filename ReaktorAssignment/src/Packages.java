
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Packages {
    private SortedMap<String, Package> installedPackages;

    public Packages() {
        installedPackages = new TreeMap<>();
    }

    public void addPackage(String name, Package pack) {
        this.installedPackages.put(name, pack);
    }

    public Set<String> getKeySet() {
        return installedPackages.keySet();
    }

    public Package getPack(String packName) {
        return installedPackages.get(packName);
    }

    public boolean containsPack(String packname) {
        return installedPackages.containsKey(packname);
    }

    public Set<Map.Entry<String,Package>> getEntrySet() {
        return this.installedPackages.entrySet();
    }
}
