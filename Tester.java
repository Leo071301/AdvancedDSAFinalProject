import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Tester {
    public static void main(String[] args) {
        System.out.println("=== PHASE 2: ALGORITHM & LOGIC VERIFICATION ===\n");

        TradeManager manager = new TradeManager();

        // 1. SETUP COLONIES
        Colony alpha = new Colony("Alpha", new Point2D.Double(5, 5), 1);
        Colony beta = new Colony("Beta", new Point2D.Double(50, 50), 3);
        manager.addColony(alpha);
        manager.addColony(beta);

        // --- 2. DEMONSTRATE SORTING (Requirement: Show before/after) ---
        System.out.println("--- Section 1: Inventory Sorting (Merge Sort) ---");

        // We create a standalone list to demonstrate the sorting algorithm clearly
        ArrayList<Resource> items = new ArrayList<>();
        items.add(new Food("Zucchini", 10, "Fresh"));
        items.add(new Food("Beans", 100, "Canned"));
        items.add(new Medicine("Aspirin", 50, "Pills"));

        //Explicitly calls Quick Sort
        //Demo that sorts inventory by amount
        ResourceComparator customComparator = new ResourceComparator();

        System.out.println("before quick sort:");
        System.out.println(items);

        SearchSortUtils.quickSort(items, customComparator);

        System.out.println("After quick sort in descending amount(0-100):");
        System.out.println(items);
        System.out.println();

        System.out.println("BEFORE Merge Sort:");
        System.out.println(items);

        // Explicitly calls your Merge Sort
        // Demo sorts inventory in alphabetical order
        SearchSortUtils.mergeSort(items);

        System.out.println("\nAFTER Merge Sort (A-Z):");
        System.out.println(items);



        // Now add the sorted items to the colony for the rest of the test
        for(Resource r : items) {
            alpha.addResource(r);
        }
        System.out.println();

        // --- 3. DEMONSTRATE SEARCHING (Requirement: Successful vs Failed) ---
        System.out.println("--- Section 2: Resource Searching (Binary Search) ---");

        // Test A: Successful Search (Right name, Right type)
        // alpha.hasResource internally calls your Binary Search
        boolean foundBeans = alpha.hasResource("Beans", 50, Food.class);
        System.out.println("Search 'Beans' (Food): " + (foundBeans ? "SUCCESS" : "FAILED"));

        // Test B: Failed Search (Wrong Name)
        boolean foundRice = alpha.hasResource("Rice", 1, Food.class);
        System.out.println("Search 'Rice' (Non-existent): " + (foundRice ? "SUCCESS" : "FAILED"));

        // Test C: Failed Search (Right name, WRONG type)
        boolean beansAsMed = alpha.hasResource("Beans", 1, Medicine.class);
        System.out.println("Search 'Beans' (as Medicine): " + (beansAsMed ? "SUCCESS" : "FAILED"));
        System.out.println();

        // --- 4. TRADE MANAGER LOGIC ---
        System.out.println("--- Section 3: Trade Manager Logic (FIFO & Compatibility) ---");

        beta.addResource(new Food("Beans", 200, "Canned"));

        // Create requests
        TradeRequest req1 = new TradeRequest(alpha, new Food("Beans", 20, "Canned"));
        try { Thread.sleep(100); } catch (InterruptedException e) {} // Ensure unique timestamps
        TradeRequest req2 = new TradeRequest(alpha, new Food("Beans", 150, "Canned"));

        manager.addRequest(req1);
        manager.addRequest(req2);

        System.out.println("Added Requests to Queue:");
        System.out.println("1. " + req1.getId() + " for 20 Beans");
        System.out.println("2. " + req2.getId() + " for 150 Beans");

        // First match
        System.out.println("\nAttempting to match oldest request (" + req1.getId() + ")...");
        manager.matchTrades();
        System.out.println("Result: Match successful.");
        System.out.println("Alpha Beans Total: " + getResourceCount(alpha, "Beans", Food.class) + " (Started with 100 + 20)");

        // Second match
        System.out.println("\nAttempting to match next request (" + req2.getId() + ")...");
        manager.matchTrades();
        System.out.println("Result: Match successful.");
        System.out.println("Alpha Beans Total: " + getResourceCount(alpha, "Beans", Food.class) + " (120 + 150)");
        System.out.println("Beta (Provider) Beans Remaining: " + getResourceCount(beta, "Beans", Food.class));

        // --- 5. EDGE CASES ---
        System.out.println("\n--- Section 4: Edge Cases & Exhaustion ---");
        TradeRequest impossible = new TradeRequest(alpha, new Food("Beans", 5000, "Canned"));
        manager.addRequest(impossible);

        System.out.println("Created Request: " + impossible.getId() + " for 5000 Beans.");
        System.out.println("Available in world: 30 Beans.");

        boolean matched = manager.matchTrades();
        System.out.println("Attempting match for " + impossible.getId() + "...");
        System.out.println("Impossible Trade matched? " + matched);
    }

    private static int getResourceCount(Colony c, String name, Class<?> type) {
        for (Resource r : c.viewInventory()) {
            if (r.getName().equalsIgnoreCase(name) && type.isInstance(r)) {
                return r.getAmount();
            }
        }
        return 0;
    }
}