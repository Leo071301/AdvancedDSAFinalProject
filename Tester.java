import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Tester {
    public static void main(String[] args) {

        System.out.println("=== PHASE 3: INTEGRATED LOGISTICS SYSTEM VERIFICATION ===\n");

        TradeManager manager = new TradeManager();

        // 1. SETUP COLONIES
        Colony alpha = new Colony("Alpha", new Point2D.Double(5, 5), 1);
        Colony beta = new Colony("Beta", new Point2D.Double(50, 50), 3);
        Colony gamma = new Colony("Gamma", new Point2D.Double(6, 6), 1);

        // This addColony method handles BOTH HashMap and AVL Tree internally
        manager.addColony(alpha);
        manager.addColony(beta);
        manager.addColony(gamma);

        // --- SECTION 1: HASHMAP & AVL TREE DEMONSTRATION ---
        System.out.println("--- Section 1: HashMap (O(1)) & AVL Tree (O(log n)) ---");

        // A. HashMap Retrieval (Requirement: Direct Access)
        Colony retrieved = manager.getColony(alpha.getId());
        System.out.println("HashMap Direct Access (ID: " + alpha.getId() + "): " +
                (retrieved != null ? "SUCCESS (" + retrieved.getName() + ")" : "FAILED"));

        // B. AVL Tree Search (Requirement: Search existing/missing)
        System.out.println("AVL Tree Search (Alpha): " + (manager.isColonyinSystem(alpha) ? "FOUND" : "NOT FOUND"));
        Colony fake = new Colony("Ghost", new Point2D.Double(0,0), 1);
        System.out.println("AVL Tree Search (Missing): " + (manager.isColonyinSystem(fake) ? "FOUND" : "NOT FOUND"));

        // C. AVL Tree Inorder Traversal (Requirement: Ordered Lookup)
        System.out.println("\nColony Registry via AVL Inorder Traversal:");
        manager.displayColonies();

        // D. HashMap/AVL Tree Deletion
        System.out.println("\nDeleting Gamma from system...");
        manager.removeColony(gamma);
        System.out.println("Gamma still in Map? " + (manager.getColony(gamma.getId()) != null));
        System.out.println("Gamma still in Tree? " + manager.isColonyinSystem(gamma));
        System.out.println();

        // --- SECTION 2: WEIGHTED GRAPH & ROUTING (DIJKSTRA) ---
        System.out.println("--- Section 2: Weighted Graph & Dijkstra's Shortest Path ---");

        // Setup at least 6 vertices for the graph requirement
        Colony delta = new Colony("Delta", new Point2D.Double(20, 10), 2);
        Colony epsilon = new Colony("Epsilon", new Point2D.Double(35, 25), 4);
        Colony zeta = new Colony("Zeta", new Point2D.Double(60, 15), 2);

        WeightedGraph<Colony> graph = new WeightedGraph<>();
        graph.addVertex(alpha);    // index 0
        graph.addVertex(beta);     // index 1
        graph.addVertex(gamma);    // index 2 (Re-added to graph for structure)
        graph.addVertex(delta);    // index 3
        graph.addVertex(epsilon);  // index 4
        graph.addVertex(zeta);     // index 5

        // Define delivery routes (Edges) with Point2D distances as weights
        graph.addEdge(0, 2, alpha.getLocation().distance(gamma.getLocation()));
        graph.addEdge(0, 3, alpha.getLocation().distance(delta.getLocation()));
        graph.addEdge(2, 3, gamma.getLocation().distance(delta.getLocation()));
        graph.addEdge(3, 4, delta.getLocation().distance(epsilon.getLocation()));
        graph.addEdge(4, 1, epsilon.getLocation().distance(beta.getLocation()));
        graph.addEdge(4, 5, epsilon.getLocation().distance(zeta.getLocation()));
        graph.addEdge(1, 5, beta.getLocation().distance(zeta.getLocation()));

        // Connect graph to the manager so it can use Dijkstra for compatibility
        manager.setLogisticsGraph(graph);

        System.out.println("Logistics Network Map:");
        graph.printEdges();

        // RUN DIJKSTRA'S ALGORITHM
        System.out.println("\nCalculating Shortest Path from Alpha (0) to Zeta (5)...");
        WeightedGraph<Colony>.ShortestPathTree pathTree = graph.getShortestPath(0);

        System.out.print("Optimal Route: ");
        pathTree.printPath(5);
        System.out.println();
        System.out.println("Total Route Distance: " + String.format("%.2f", pathTree.getCost(5)) + " units.");

        // --- SECTION 3: MINIMUM SPANNING TREE (PRIM'S) ---
        System.out.println("\n--- Section 3: Minimum Spanning Tree (Prim's Algorithm) ---");
        System.out.println("To ensure all colonies can trade, a minimum of "
                + String.format("%.2f", manager.reportMinimumRoadDistance()) + " km of road must be secured.");
        System.out.println("Note: This connects all colonies with the lowest total infrastructure cost.");

        // --- SECTION 4: REGRESSION TESTING (PHASE 2 LOGIC) ---
        System.out.println("\n--- Section 4: Phase 2 Logic Verification (Sorting & Trading) ---");

        // Setup inventory for Alpha
        ArrayList<Resource> items = new ArrayList<>();
        items.add(new Food("Zucchini", 10, "Fresh"));
        items.add(new Medicine("Aspirin", 50, "Pills"));
        items.add(new Food("Beans", 100, "Canned"));

        SearchSortUtils.mergeSort(items);
        for(Resource r : items) { alpha.addResource(r); }
        beta.addResource(new Food("Beans", 200, "Canned"));

        TradeRequest req = new TradeRequest(alpha, new Food("Beans", 50, "Canned"));
        manager.addRequest(req);

        boolean success = manager.matchTrades();
        System.out.println("Trade matching (FIFO): " + (success ? "SUCCESS" : "FAILED"));
        System.out.println("Alpha final Beans: " + getResourceCount(alpha, "Beans", Food.class));

        // --- PERFORMANCE BENCHMARK ---
        runBenchmark();
    }

    private static void runBenchmark() {
        System.out.println("\n--- Section 5: Performance Benchmark (10,000 Items) ---");
        int dataSize = 10_000;
        ArrayList<Resource> list = new ArrayList<>();
        for (int i = 0; i < dataSize; i++) {
            list.add(new Food("Item" + (int)(Math.random() * 10000), i, "Bulk"));
        }

        long start = System.currentTimeMillis();
        SearchSortUtils.mergeSort(list);
        long end = System.currentTimeMillis();
        System.out.println("Manual Merge Sort Time: " + (end - start) + " ms");
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
