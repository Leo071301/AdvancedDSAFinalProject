import java.awt.geom.Point2D;
import java.util.Map;

public class Tester {
    public static void main(String[] args) {

        System.out.println("=== 1) Instantiate concrete objects ===");
        Colony farm = new Colony("Farm", new Point2D.Double(100, 200), 5);
        Colony city = new Colony("City", new Point2D.Double(100, 300), 3);

        System.out.println(farm + "\n");
        System.out.println(city + "\n");

        System.out.println("=== 2) Polymorphism: supertype variable -> subtype object ===");
        Resource r1 = new Food("Beans", 50, "Canned");                 // Resource -> Food
        Resource r2 = new Medicine("Antibiotics", 20, "Pills");        // Resource -> Medicine

        // Add resources to inventory using Resource-typed variables (polymorphism)
        farm.addResource(r1);
        farm.addResource(r2);

        System.out.println("Added to farm inventory via Resource variables.\n");

        System.out.println("=== 3) instanceof + safe casting ===");
        if (r1 instanceof Food) {
            Food f = (Food) r1; // safe cast
            System.out.println("r1 is Food, type = " + f.getType());
        }

        if (r2 instanceof Medicine) {
            Medicine m = (Medicine) r2; // safe cast
            System.out.println("r2 is Medicine, type = " + m.getType());
        }
        System.out.println();

        System.out.println("=== 4) Print state to verify logic (IDs, copying, amounts) ===");

        // Print view-only inventory (copies) and show contents
        System.out.println("Farm Inventory: " + farm.viewInventory() + "\n");

        // Verify ID assignment is working (C001, C002, TR001, TR002 ...)
        TradeRequest tr1 = new TradeRequest(farm, new Food("Beans", 10, "Canned"));
        TradeRequest tr2 = new TradeRequest(city, new Medicine("Antibiotics", 5, "Pills"));

        System.out.println(tr1 + "\n");
        System.out.println(tr2 + "\n");

        // Verify that modifying the original object should NOT affect colony inventory
        System.out.println("=== Extra check: confirm inventory uses copies ===");
        System.out.println("Before external modification: " + r1); // original r1

        r1.addAmount(1000); // modifies the original object reference we still hold
        System.out.println("After external modification to r1: " + r1);

        // Farm's inventory should NOT jump by +1000 since we encapsulated farm's inventory
        System.out.println("\nFarm inventory view again (should be unchanged by r1 modification):");
        Map<String, Resource> invView2 = farm.viewInventory();
        for (Map.Entry<String, Resource> entry : invView2.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
