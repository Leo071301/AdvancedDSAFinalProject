import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Colony implements Comparable<Colony>, Identifiable {
    private final String colony_id; // e.g. C001
    private String name;
    private final Point2D location; // Coordinates of colony's location
    private int riskFactor; // 1-5: 1 safest and 5 the most dangerous
    private ArrayList<Resource> inventory = new ArrayList<>();
    private static int nextId = 1;

    // No-arg constructor
    public Colony() {
        this("Unknown Colony", null, 1);
    }

    // Overloaded constructor
    public Colony(String name, Point2D location, int riskFactor) {
        if (riskFactor < 1 || riskFactor > 5) {
            throw new IllegalArgumentException("Risk factor must be between 1 and 5");
        }
        this.colony_id = "C" + String.format("%03d", nextId++); // generate colony's unique id when created
        this.location = location;
        this.name = name;
        this.riskFactor = riskFactor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point2D getLocation() {
        return location;
    }

    public int getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(int riskFactor) {
        if  (riskFactor < 1 || riskFactor > 5) {
            throw new IllegalArgumentException("Risk factor must be between 1 and 5");
        }
        this.riskFactor = riskFactor;
    }

    @Override
    public String getId() {
        return colony_id;
    }

    // Add resources to the inventory
    public void addResource(Resource resource) {
        sortInventoryByName();
        int index = SearchSortUtils.binarySearchResource(inventory, resource.getName(), resource.getClass());

        if (index != -1) {
            // Match found, so update the amount
            inventory.get(index).addAmount(resource.getAmount());
        } else {
            // No match found, add a new copy
            inventory.add(resource.copy());
        }
        // Ensure that inventory is sorted A-Z after adding items
        sortInventoryByName();
    }

    // Remove resources from the inventory
    public boolean removeResource(String resourceName, int amount, Class<?> type) {
        sortInventoryByName();
        int index = SearchSortUtils.binarySearchResource(inventory, resourceName, type);

        if (index != -1 && inventory.get(index).getAmount() >= amount) {
            inventory.get(index).removeAmount(amount);
            if (inventory.get(index).getAmount() == 0) {
                inventory.remove(index);
            }
            return true;
        }
        return false;
    }

    // Check whether the colony's inventory contains at least "amount" of that resource
    public boolean hasResource(String resourceName, int amount, Class<?> type) {
        sortInventoryByName();
        int index = SearchSortUtils.binarySearchResource(inventory, resourceName, type);

        if (index != -1) {
            return inventory.get(index).getAmount() >= amount;
        }
        return false;
    }

    // Return safe copy of inventory
    public ArrayList<Resource> viewInventory() {
        ArrayList<Resource> copyList = new ArrayList<>();
        for (Resource r : inventory) {
            copyList.add(r.copy());
        }
        return copyList;
    }

    // Sort the inventory by name using Merge Sort
    public void sortInventoryByName() {
        SearchSortUtils.mergeSort(this.inventory);
    }

    @Override
    public String toString() {
        return "Colony: " + name + "\nColony ID: " + colony_id + "\nLocation: (" + location.getX()
                + ", " + location.getY() + ")" + "\nRisk Factor: " + riskFactor;
    }

    @Override
    public int compareTo(Colony o) {
        return this.colony_id.compareTo(o.colony_id);
    }

    @Override
    public boolean equals(Object o) {
        // Check if same address in memory
        if (this == o) return true;

        // Check if other colony is null or not the same class
        if (o == null || getClass() != o.getClass()) return false;

        Colony other = (Colony) o;

        // Check if colony IDs are equal
        return this.colony_id.equals(other.colony_id);
    }

    @Override
    public int hashCode() {
        return colony_id.hashCode();
    }
}
