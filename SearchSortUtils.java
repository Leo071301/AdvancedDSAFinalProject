import java.util.ArrayList;

public class SearchSortUtils {

    // --- MERGE SORT ---
    public static <T extends Comparable<T>> void mergeSort(ArrayList<T> list) {
        if (list == null || list.size() <= 1) return;

        ArrayList<T> sorted = recursiveMergeSort(list);

        // Update the original list's content
        list.clear();
        list.addAll(sorted);
    }

    private static <T extends Comparable<T>> ArrayList<T> recursiveMergeSort(ArrayList<T> list) {
        if (list.size() <= 1) return list;

        int mid = list.size() / 2;
        ArrayList<T> left = new ArrayList<>(list.subList(0, mid));
        ArrayList<T> right = new ArrayList<>(list.subList(mid, list.size()));

        return merge(recursiveMergeSort(left), recursiveMergeSort(right));
    }

    private static <T extends Comparable<T>> ArrayList<T> merge(ArrayList<T> left, ArrayList<T> right) {
        ArrayList<T> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).compareTo(right.get(j)) <= 0) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }
        merged.addAll(left.subList(i, left.size()));
        merged.addAll(right.subList(j, right.size()));
        return merged;
    }

    // --- BINARY SEARCH BY RESOURCE ---
    public static <T extends Resource> int binarySearchResource(ArrayList<T> list, String targetName, Class<?> targetClass) {
        return recursiveBinarySearchResource(list, targetName, targetClass, 0, list.size() - 1);
    }

    // Recursive helper method
    private static <T extends Resource> int recursiveBinarySearchResource(ArrayList<T> list, String targetName, Class<?> targetClass, int low, int high) {
        // Base case: target not found
        if (low > high) {
            return -1;
        }

        int mid = low + (high - low) / 2;
        Resource midRes = list.get(mid);
        int comp = midRes.getName().compareToIgnoreCase(targetName);

        if (comp == 0) {
            // If names match, check if the Class type also matches or probe
            if (midRes.getClass().equals(targetClass)) {
                return mid;
            }
            // If names match but classes don't, check adjacent items
            // (Linear probe) because merge sort grouped them by name
            return linearProbe(list, mid, targetName, targetClass);
        }
        // Recursive case
        if (comp < 0) {
            return recursiveBinarySearchResource(list, targetName, targetClass, mid + 1, high);
        } else  {
            return recursiveBinarySearchResource(list, targetName, targetClass, low, mid-1);
        }
    }

    // --- BINARY SEARCH BY ID ---
    public static <T extends Identifiable> int binarySearchById(ArrayList<T> list, String targetId) {
        return recursiveBinarySearchById(list, targetId, 0, list.size() - 1);
    }

    // Recursive helper method
    private static <T extends Identifiable> int recursiveBinarySearchById(ArrayList<T> list, String targetId, int low, int high) {
        // Base Case: Target not found
        if (low > high) {
            return -1;
        }

        int mid = low + (high - low) / 2;
        int comp = list.get(mid).getId().compareTo(targetId);

        // Base Case: Target found
        if (comp == 0) {
            return mid;
        }

        // Recursive Cases
        if (comp < 0) {
            return recursiveBinarySearchById(list, targetId, mid + 1, high);
        } else {
            return recursiveBinarySearchById(list, targetId, low, mid - 1);
        }
    }

    private static <T extends Resource> int linearProbe(ArrayList<T> list, int start, String name, Class<?> targetClass) {
        // Check surrounding items with the same name for the correct class
        for (int i = start; i < list.size() && list.get(i).getName().equalsIgnoreCase(name); i++) {
            if (list.get(i).getClass().equals(targetClass)) return i;
        }
        for (int i = start - 1; i >= 0 && list.get(i).getName().equalsIgnoreCase(name); i--) {
            if (list.get(i).getClass().equals(targetClass)) return i;
        }
        return -1;
    }
}
