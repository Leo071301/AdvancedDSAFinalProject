import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Colony implements Identifiable {
    private final String colony_id; // e.g. C001
    private String name;
    private final Point2D location;
    private int riskFactor; // 1-5: 1 safest and 5 the most dangerous
    private HashMap<String, Resource> inventory = new HashMap<>();
    private static int nextId = 1;

    public Colony(String name, Point2D location, int riskFactor) {
        if (riskFactor < 1 || riskFactor > 5) {
            throw new IllegalArgumentException("Risk factor must be between 1 and 5");
        }
        this.colony_id = "C" + String.format("%03d", nextId++);
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
        String key = generateKey(resource);
        if (inventory.containsKey(key)) { // Add the resource to inventory if it is already in the inventory
            inventory.get(key).addAmount(resource.getAmount());
        } else { // Put the resource in inventory
            inventory.put(key, resource.copy());
        }
    }

    // Remove resources from the inventory
    public void removeResource(Resource resource) {
        String key = generateKey(resource);

        if (!inventory.containsKey(key)) {
            throw new IllegalArgumentException(resource + " does not exist!");
        }

        Resource existing = inventory.get(key);

        if (resource.getAmount() >= existing.getAmount()) {
            // Remove the resource entirely if requested amount >= stored amount
            inventory.remove(key);
        } else {
            // Otherwise, just decrease the amount
            existing.removeAmount(resource.getAmount());
        }
    }

    // Check whether the colony's inventory contains at least "amount" of that resource
    public boolean hasResource(Resource resource, int amount) {
        String key = generateKey(resource);
        Resource existing = inventory.get(key);
        return existing != null && existing.getAmount() >= amount;
    }

    // Return a fully safe, read-only view of the inventory through copies
    public Map<String, Resource> viewInventory() {
        Map<String, Resource> inventoryCopy = new HashMap<>();
        for (Map.Entry<String, Resource> entry : inventory.entrySet()) {
            inventoryCopy.put(entry.getKey(), entry.getValue().copy());
        }
        return Collections.unmodifiableMap(inventoryCopy);
    }

    // Create a unified, formatted key
    private String generateKey(Resource resource) {
        return resource.getClass().getSimpleName().toUpperCase()
                + ":" + resource.getName();
    }

    @Override
    public String toString() {
        return "Colony: " + name + "\nColony ID: " + colony_id + "\nLocation: (" + location.getX()
                + ", " + location.getY() + ")" + "\nRisk Factor: " + riskFactor;
    }
}
