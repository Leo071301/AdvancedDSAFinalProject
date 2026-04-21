import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tester {
    public static void main(String[] args) {

        System.out.println("=== PHASE 2: ALGORITHM & LOGIC VERIFICATION ===\n");

        TradeManager manager = new TradeManager();

        // 1. SETUP COLONIES
        Colony alpha = new Colony("Alpha", new Point2D.Double(5, 5), 1);
        Colony beta = new Colony("Beta", new Point2D.Double(50, 50), 3);
        manager.addColony(alpha);
        manager.addColony(beta);

        // --- 2. DEMONSTRATE SORTING ---
        System.out.println("--- Section 1: Inventory Sorting (Merge Sort) ---");

        // Create a standalone list to demonstrate the sorting algorithm clearly
        ArrayList<Resource> items = new ArrayList<>();
        items.add(new Food("Zucchini", 10, "Fresh"));
        items.add(new Medicine("Aspirin", 50, "Pills"));
        items.add(new Food("Beans", 100, "Canned"));

        System.out.println("BEFORE Merge Sort:");
        System.out.println(items);

        SearchSortUtils.mergeSort(items);

        System.out.println("\nAFTER Merge Sort (A-Z):");
        System.out.println(items);

        // Add the sorted items to the colony for the rest of the test
        for(Resource r : items) {
            alpha.addResource(r);
        }
        System.out.println();

        // --- 3. DEMONSTRATE SEARCHING ---
        System.out.println("--- Section 2: Resource Searching (Binary Search) ---");

        // Test A: Successful Search (Right name, Right type)
        boolean foundBeans = alpha.hasResource("Beans", 50, Food.class);
        System.out.println("Search 'Beans' (Food): " + (foundBeans ? "SUCCESS" : "FAILED"));

        // Test B: Failed Search (Wrong Name)
        boolean foundRice = alpha.hasResource("Rice", 1, Food.class);
        System.out.println("Search 'Rice' (Non-existent): " + (foundRice ? "SUCCESS" : "FAILED"));

        // Test C: Failed Search (Right name, WRONG type)
        boolean beansAsMed = alpha.hasResource("Beans", 1, Medicine.class);
        System.out.println("Search 'Beans' (as Medicine): " + (beansAsMed ? "SUCCESS" : "FAILED"));

        // --- 4. TRADE MANAGER LOGIC ---
        System.out.println("\n--- Section 3: Trade Manager Logic (FIFO & Compatibility) ---");

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
        System.out.println("Trade matched? " + matched);

        // --- Identity & Hash Verification ---
        System.out.println("\n--- Section 5: Identity & Hash Verification ---");

        // 1. Verify Uniqueness (Static counter check)
        System.out.println("Alpha ID: " + alpha.getId());
        System.out.println("Beta ID: " + beta.getId());
        System.out.println("IDs are unique? " + (!alpha.getId().equals(beta.getId())));

        // 2. Verify hashCode (Should be different for different IDs)
        int hashA = alpha.hashCode();
        int hashB = beta.hashCode();
        System.out.println("Alpha Hash: " + hashA);
        System.out.println("Beta Hash: " + hashB);
        System.out.println("Hashes are different? " + (hashA != hashB));

        // 3. Verify equals() Contract
        // Since they have different IDs, they MUST not be equal
        System.out.println("Alpha equals Beta? " + alpha.equals(beta));

        // 4. Verify HashMap Integrity
        java.util.HashMap<Colony, String> colonyMap = new java.util.HashMap<>();
        colonyMap.put(alpha, "Primary Hub");
        System.out.println("HashMap can find Alpha? " + colonyMap.containsKey(alpha));

        // --- Custom Comparator Verification ---
        System.out.println("\n--- Section 6: Provider Ranking (Custom Comparator) ---");

        // 1. Set up a "Choice" scenario
        Colony gamma = new Colony("Gamma", new Point2D.Double(6, 6), 1); // Very close & safe
        manager.addColony(gamma);
        gamma.addResource(new Food("Beans", 100, "Canned"));

        // 2. Create a request that both Beta and Gamma can fulfill
        TradeRequest choiceReq = new TradeRequest(alpha, new Food("Beans", 10, "Canned"));

        // 3. Manually call ranking logic to see the sorted list
        List<Colony> candidates = manager.findCandidates(choiceReq);
        // Calls the Comparator sort internally in chooseBestProvider
        Colony best = manager.chooseBestProvider(choiceReq, candidates);

        System.out.println("Candidates found: " + candidates.size());
        System.out.println("Top Ranked Provider: " + best.getName() + " (ID: " + best.getId() + ")");
        System.out.println("Ranking Check: " + (best == gamma ? "SUCCESS (Gamma is better than Beta)" : "FAILED"));

        // --- Benchmarking Experiment (Requirement: 10,000+ objects) ---
        System.out.println("\n--- Performance Benchmark ---");

        int dataSize = 10_000; // Requirement: 10,000 or more
        ArrayList<Resource> largeList1 = new ArrayList<>();

        // Generate dataset
        for (int i = 0; i < dataSize; i++) {
            // Using random-ish names to ensure the sort has work to do
            largeList1.add(new Food("Item" + (int)(Math.random() * 10000), i, "Bulk"));
        }

        // Create a copy so both tests start with the same unsorted data
        ArrayList<Resource> largeList2 = new ArrayList<>(largeList1);

        // Test 1: Manual Sort (Merge Sort)
        long startManual = System.currentTimeMillis();
        SearchSortUtils.mergeSort(largeList1);
        long endManual = System.currentTimeMillis();
        long manualDuration = endManual - startManual;

        // Test 2: Java's Built-in Sort
        long startJava = System.currentTimeMillis();
        Collections.sort(largeList2);
        long endJava = System.currentTimeMillis();
        long javaDuration = endJava - startJava;

        // Report results
        System.out.println("Dataset Size: " + dataSize);
        System.out.println("Manual Merge Sort Time: " + manualDuration + " ms");
        System.out.println("Java Collections.sort() Time: " + javaDuration + " ms");
        System.out.println("Difference: " + (manualDuration - javaDuration) + " ms");


        // --- Phase 3: Weighted Graph Foundation ---
        System.out.println("\n--- Section 7: Weighted Graph Foundation ---");

        // Create more colonies so the graph has at least 6 vertices
        Colony delta = new Colony("Delta", new Point2D.Double(20, 10), 2);
        Colony epsilon = new Colony("Epsilon", new Point2D.Double(35, 25), 4);
        Colony zeta = new Colony("Zeta", new Point2D.Double(60, 15), 2);

        // Build the graph
        WeightedGraph<Colony> graph = new WeightedGraph<>();

        graph.addVertex(alpha);    // index 0
        graph.addVertex(beta);     // index 1
        graph.addVertex(gamma);    // index 2
        graph.addVertex(delta);    // index 3
        graph.addVertex(epsilon);  // index 4
        graph.addVertex(zeta);     // index 5

        // Add undirected weighted edges using location distance as weight
        graph.addEdge(0, 2, alpha.getLocation().distance(gamma.getLocation()));
        graph.addEdge(0, 3, alpha.getLocation().distance(delta.getLocation()));
        graph.addEdge(2, 3, gamma.getLocation().distance(delta.getLocation()));
        graph.addEdge(3, 4, delta.getLocation().distance(epsilon.getLocation()));
        graph.addEdge(4, 1, epsilon.getLocation().distance(beta.getLocation()));
        graph.addEdge(4, 5, epsilon.getLocation().distance(zeta.getLocation()));
        graph.addEdge(1, 5, beta.getLocation().distance(zeta.getLocation()));

        // Print the graph structure
        System.out.println("Graph edges:");
        graph.printEdges();

        // Check neighbors of Alpha
        System.out.println("\nNeighbors of Alpha:");
        List<Integer> alphaNeighbors = graph.getNeighbors(graph.getIndex(alpha));
        for (int neighborIndex : alphaNeighbors) {
            System.out.println("- " + graph.getVertex(neighborIndex).getName());
        }

        // Check sample weights
        System.out.println("\nSample edge weights:");
        System.out.println("Alpha <-> Gamma: " + graph.getWeight(0, 2));
        System.out.println("Delta <-> Epsilon: " + graph.getWeight(3, 4));
        System.out.println("Beta <-> Zeta: " + graph.getWeight(1, 5));

        // Verify graph size
        System.out.println("\nTotal vertices in graph: " + graph.getSize());

        // --- Section 8: Minimum Spanning Tree (Prim's) ---
        System.out.println("\n--- Section 8: Minimum Spanning Tree (Prim's) ---");

        // Run Prim starting from Alpha (index 0)
        WeightedGraph<Colony>.MST mst = graph.getMinimumSpanningTree();

        // Print the MST
        mst.printTree();

        // Optional: explicitly print total weight again for clarity
        System.out.println("Verified Total Weight: " + mst.getTotalWeight());
    }

    // helper method for displaying resource counts
    private static int getResourceCount(Colony c, String name, Class<?> type) {
        for (Resource r : c.viewInventory()) {
            if (r.getName().equalsIgnoreCase(name) && type.isInstance(r)) {
                return r.getAmount();
            }
        }
        return 0;
    }
}
