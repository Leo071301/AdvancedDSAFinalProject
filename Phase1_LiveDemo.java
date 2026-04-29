import java.awt.geom.Point2D;
import java.util.Scanner;
import java.util.Map;

public class Phase1_LiveDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("----------------------------------------------------");
        System.out.println("       PHASE 1: INTERACTIVE OOP DEMONSTRATION       ");
        System.out.println("----------------------------------------------------");
        System.out.println();

        // encapsulation
        System.out.println("PART 1: INSTANTIATE COLONY");
        System.out.println("--------------------------");
        System.out.print("Enter Colony name: ");
        String colName = sc.nextLine();

        System.out.print("Enter X coordinate: ");
        double x = sc.nextDouble();

        System.out.print("Enter Y coordinate: ");
        double y = sc.nextDouble();

        System.out.print("Enter risk factor: ");
        int rsk = sc.nextInt();
        sc.nextLine(); // Clear buffer

        // Data is hidden inside the object; accessed only via constructor/getters
        Colony userColony = new Colony(colName, new Point2D.Double(x, y), rsk);

        System.out.println("\nColony Status:");
        System.out.println("Name: " + userColony.getName());
        System.out.println("Location: " + userColony.getLocation());
        System.out.println("Risk Factor: " + rsk);
        System.out.println();

        //INHERITANCE & POLYMORPHISM
        System.out.println("PART 2: INHERITANCE & POLYMORPHISM");
        System.out.println("----------------------------------");
        System.out.print("Enter Food name: ");
        String fName = sc.nextLine();

        System.out.print("Enter Food amount: ");
        int fAmt = sc.nextInt();
        sc.nextLine(); // Clear buffer

        System.out.print("Enter Food type (e.g. vegetable): ");
        String fTyp = sc.nextLine();

        Resource userFood = new Food(fName, fAmt, fTyp);

        System.out.println("\nObject Analysis:");
        System.out.println("Class Type: " + userFood.getClass().getSimpleName());
        System.out.println("Inherited Name: " + userFood.getName());

        // Adding to colony demonstrates polymorphic behavior in the addResource(Resource r) method
        userColony.addResource(userFood);
        System.out.println("Status: Resource successfully added to colony inventory.");
        System.out.println();

        // encapsulation
        System.out.println("PART 3: ENCAPSULATION & DEFENSIVE COPYING");
        System.out.println("-----------------------------------------");
        System.out.print("Enter amount to add to your local " + fName + " object: ");
        int modAmount = sc.nextInt();

        System.out.println("\nTracking Changes:");
        System.out.println("Local amount before modification: " + userFood.getAmount());

        userFood.addAmount(modAmount);

        System.out.println("Local amount after modification:  " + userFood.getAmount());

        // Verify the Colony's internal state was protected
        Map<String, Resource> inventory = userColony.viewInventory();
        Resource internalRef = null;

        for (Resource r : inventory.values()) {
            if (r.getName().equalsIgnoreCase(fName)) {
                internalRef = r;
                break;
            }
        }

        if (internalRef != null) {
            System.out.println("Colony's internal stored amount:  " + internalRef.getAmount());
            System.out.println();

            if (internalRef.getAmount() != userFood.getAmount()) {
                System.out.println("VERIFICATION: SUCCESS");
                System.out.println("Encapsulation preserved. The colony holds a separate copy.");
            } else {
                System.out.println("VERIFICATION: FAILED");
                System.out.println("The colony is sharing a reference with the local object.");
            }
        } else {
            System.out.println("VERIFICATION: ERROR");
            System.out.println("Resource could not be located in colony inventory.");
        }

        System.out.println("\n----------------------------------------------------");
        sc.close();
    }
}