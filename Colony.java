import java.awt.geom.Point2D;
import java.util.Map;

public class Colony implements Identifiable {
    private final String colony_id; // e.g. C001
    private String name;
    private final Point2D location;
    private int riskFactor; // 1-5: 1 safest and 5 the most dangerous
    private HashMap<String, Resource> inventory;

    public Colony(String colony_id, String name, Point2D location, int riskFactor) {
        if (colony_id == null) {
            throw new IllegalArgumentException("Colony ID cannot be null");
        }
        if (riskFactor < 1 || riskFactor > 5) {
            throw new IllegalArgumentException("Risk factor must be between 1 and 5");
        }
        this.colony_id = colony_id;
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
        this.riskFactor = riskFactor;
    }

    @Override
    public String getId() {
        return colony_id;
    }

    // TODO: add resources to the inventory
    public void addResource(String key, int amount) {}

    // TODO: remove resources from the inventory
    public void removeResource(String key, int amount) {}

    // TODO: check whether the colony's inventory contains a resource and how much of it
    public boolean hasResource(String key, int amount) { return false; }

    // TODO: return a Map of inventory (view-only, use Collections.unmodifiable)
    public Map viewInventory() { return Map.of(); }

    // TODO: create a trade request asking for a specific resource and the amount
    public TradeRequest createTradeRequest(String key, int amount) { return new TradeRequest(this, key, amount); }
}
