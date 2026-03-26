import java.util.Comparator;

public class ResourceComparator implements Comparator<Resource> {
    @Override
    public int compare(Resource r1, Resource r2) {
        // compares resources by amount
        return Integer.compare(r1.getAmount(), r2.getAmount());
    }
}