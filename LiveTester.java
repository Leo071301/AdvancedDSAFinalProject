import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class LiveTester {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   COLONY TRADE SYSTEM: PHASE 2 DIAGNOSTIC   ");
        System.out.println("==============================================\n");

        // --- STEP 1: INITIALIZATION ---
        System.out.println("[LOG] Initializing TradeManager and Colonies...");
        TradeManager manager = new TradeManager();
        Colony alpha = new Colony("Alpha-1", new Point2D.Double(5, 5), 1);
        System.out.println("[SUCCESS] Environment ready.\n");

        // --- STEP 2: DATA GENERATION ---
        System.out.println("--- Section 1: Data Generation ---");
        System.out.print("Enter the number of resources to generate (e.g., 10000): ");
        int dataSize = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("\n[LOG] Generating " + dataSize + " randomized resource objects...");
        ArrayList<Resource> inventory = new ArrayList<>();
        for (int i = 0; i < dataSize; i++) {
            String randomName = "Res-" + (int)(Math.random() * 900000 + 100000);
            inventory.add(new Food(randomName, (int)(Math.random() * 100), "Standard Grade"));
        }

        // Pick a target that definitely exists for search comparison
        String targetName = inventory.get(dataSize / 2).getName();
        System.out.println("[SUCCESS] Generation complete.");

        pressEnterToContinue("view the 'Unsorted' state");

        // --- STEP 3: PRE-SORT VIEW ---
        System.out.println("\n--- Section 2: Current Inventory State (BEFORE) ---");
        printInventorySample(inventory);

        pressEnterToContinue("test LINEAR SEARCH on unsorted data");

        // --- STEP 4: LINEAR SEARCH ---
        System.out.println("\n--- Section 3: Linear Search Verification ---");
        System.out.println("[LOG] Target for search: " + targetName);
        System.out.println("[LOG] Running Linear Search (O(n))...");

        long linStart = System.nanoTime();
        boolean foundLin = false;
        int linIndex = -1;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getName().equals(targetName)) {
                foundLin = true;
                linIndex = i;
                break;
            }
        }
        long linEnd = System.nanoTime();

        System.out.println(">> Result: " + (foundLin ? "Found at index [" + linIndex + "]" : "Not Found"));
        System.out.println(">> Efficiency: " + (linEnd - linStart) / 1000.0 + " microseconds");

        pressEnterToContinue("execute the Merge Sort algorithm");

        // --- STEP 5: SORTING ---
        System.out.println("\n--- Section 4: Inventory Sorting ---");
        System.out.println("[LOG] Executing Merge Sort (O(n log n))... Please wait.");
        long startSort = System.currentTimeMillis();
        SearchSortUtils.mergeSort(inventory);
        long endSort = System.currentTimeMillis();

        System.out.println("[SUCCESS] Sorting finalized.");
        System.out.println(">> Total Time Elapsed: " + (endSort - startSort) + " ms");

        pressEnterToContinue("verify the 'Sorted' state");

        // --- STEP 6: POST-SORT VIEW ---
        System.out.println("\n--- Section 5: Current Inventory State (AFTER) ---");
        printInventorySample(inventory);
        System.out.println("[INFO] Notice the resources are now organized alphabetically.");

        pressEnterToContinue("test BINARY SEARCH efficiency");

        // --- STEP 7: BINARY SEARCH ---
        System.out.println("\n--- Section 6: Binary Search Verification ---");
        System.out.println("[LOG] Target for search: " + targetName);
        System.out.println("[LOG] Running Binary Search (O(log n))...");

        long bStart = System.nanoTime();
        int resultIndex = Collections.binarySearch(inventory, new Food(targetName, 0, ""));
        long bEnd = System.nanoTime();

        System.out.println(">> Result: Item found at index [" + resultIndex + "]");
        System.out.println(">> Efficiency: " + (bEnd - bStart) / 1000.0 + " microseconds");
        System.out.println("[INFO] Compare this to the Linear Search time from Section 3!");

        System.out.println("\n==============================================");
        System.out.println("           ALL TESTS COMPLETED               ");
        System.out.println("==============================================");
    }

    /**
     * Helper to pause execution and wait for user acknowledgment.
     */
    private static void pressEnterToContinue(String action) {
        System.out.println("\n[PAUSED] Press ENTER to " + action + "...");
        scanner.nextLine();
    }

    /**
     * Prints the full list if small, or a summary if large.
     */
    private static void printInventorySample(ArrayList<Resource> list) {
        int size = list.size();
        if (size == 0) {
            System.out.println("Inventory is currently empty.");
            return;
        }

        if (size <= 15) {
            System.out.println("Full Inventory Display:");
            for (Resource r : list) {
                System.out.println("  - " + r.getName());
            }
        } else {
            System.out.println("Truncated View (Large Dataset):");
            for (int i = 0; i < 5; i++) {
                System.out.println("  [" + i + "] " + list.get(i).getName());
            }
            System.out.println("  ... [ " + (size - 10) + " items hidden ] ...");
            for (int i = size - 5; i < size; i++) {
                System.out.println("  [" + i + "] " + list.get(i).getName());
            }
        }
    }
}
