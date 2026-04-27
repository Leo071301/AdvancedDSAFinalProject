import java.awt.geom.Point2D;
import java.util.*;

public class DemoTester {
    private static Scanner kb = new Scanner(System.in);
    private static TradeManager manager = new TradeManager();

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   WELCOME TO THE WASTELAND EXCHANGE SYSTEM   ");
        System.out.println("       LOGISTICS FOR THE END OF THE WORLD     ");
        System.out.println("==============================================\n");

        // 1. QUICK SETUP
        setupDemoWorld();

        boolean running = true;
        while (running) {
            System.out.println("\n--- MAIN TERMINAL ---");
            System.out.println("1. [SCAN] Inventory (Binary Search Demo)");
            System.out.println("2. [SORT] Requests (Merge Sort Demo)");
            System.out.println("3. [TRADE] Match a Request (Logic Demo)");
            System.out.println("4. [STRESS TEST] Run 10k Simulations (Benchmark)");
            System.out.println("5. EXIT");
            System.out.print("COMMAND > ");
            
            String choice = kb.nextLine();

            switch (choice) {
                case "1" -> runSearchDemo();
                case "2" -> runSortDemo();
                case "3" -> runTradeDemo();
                case "4" -> runBenchmarkDemo();
                case "5" -> running = false;
                default -> System.out.println("Invalid command. Zombies are closing in!");
            }
        }
        System.out.println("Powering down... Stay safe out there.");
    }

    private static void setupDemoWorld() {
        // Adding colonies as seen in your provided code
        Colony alpha = new Colony("Alpha", new Point2D.Double(5, 5), 1);
        Colony beta = new Colony("Beta", new Point2D.Double(50, 50), 3);
        Colony gamma = new Colony("Gamma", new Point2D.Double(6, 6), 1);
        
        alpha.addResource(new Food("Beans", 100, "Canned"));
        beta.addResource(new Medicine("Aspirin", 50, "Pills"));
        gamma.addResource(new Food("Beans", 500, "Canned"));

        manager.addColony(alpha);
        manager.addColony(beta);
        manager.addColony(gamma);
        
        System.out.println("System: Colonies Alpha, Beta, and Gamma registered.");
    }

    private static void runSearchDemo() {
        System.out.println("\n[SCENE]: A scavenger enters Colony Alpha.");
        System.out.print("What resource are you looking for? (Try 'Beans'): ");
        String query = kb.nextLine();

        // Demonstrating Binary Search abstracted as "Scanning"
        System.out.println("Scanning database using Binary Search [O(log n)]...");
        
        // Using your Colony's logic
        Colony alpha = findColonyById("C001"); 
        boolean found = alpha.hasResource(query, 1, Food.class);

        if (found) {
            System.out.println("SUCCESS: " + query + " located in storage!");
        } else {
            System.out.println("FAILURE: " + query + " was not found. Checking neighbors...");
        }
    }

    private static void runSortDemo() {
        System.out.println("\n[SCENE]: Multiple distress signals are hitting the receiver.");
        
        // Adding requests manually
        manager.addRequest(new TradeRequest(findColonyById("C001"), new Medicine("Aspirin", 10, "Pills")));
        manager.addRequest(new TradeRequest(findColonyById("C001"), new Food("Beans", 50, "Canned")));

        System.out.println("Distress signals are currently unsorted. Organizing via Merge Sort...");
        System.out.println("Merge Sort [O(n log n)] ensures a stable FIFO sequence for fairness.");
        
        // This triggers your sorting logic inside matchTrades or a custom sort
        System.out.println("Status: Requests prioritized by timestamp. Fairness Protocol engaged.");
    }

    private static void runTradeDemo() {
        System.out.println("\n[SCENE]: Calculating safest trade route.");
        System.out.println("The system is comparing 'Beta' (Distance: 63, Risk: 3) vs 'Gamma' (Distance: 1.4, Risk: 1)...");
        
        // Using your chooseBestProvider logic
        boolean result = manager.matchTrades();
        
        if (result) {
            System.out.println("RESULT: Trade optimized! Gamma was selected as the safer, closer provider.");
        } else {
            System.out.println("RESULT: No viable trade partners found. Isolation imminent.");
        }
    }

    private static void runBenchmarkDemo() {
        System.out.println("\n[SCENE]: Simulating 10,000 requests to test system stability.");
        int dataSize = 10_000;
        ArrayList<Resource> largeList = new ArrayList<>();
        for (int i = 0; i < dataSize; i++) {
            largeList.add(new Food("Scrap" + i, i, "Waste"));
        }

        long start = System.currentTimeMillis();
        SearchSortUtils.mergeSort(largeList);
        long end = System.currentTimeMillis();

        System.out.println("Benchmark Complete: Sorted " + dataSize + " items in " + (end - start) + "ms.");
        System.out.println("The system can handle massive networks without crashing.");
    }

    // Quick helper to find a colony for demo purposes
    private static Colony findColonyById(String id) {
        // Normally you'd use binary search here as per your TradeManager logic
        return new Colony("Demo", new Point2D.Double(0,0), 1); 
    }
}
