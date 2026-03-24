public class Food extends Resource {

    private String type; // e.g., "Pills", "Canned", etc.

    // No-arg constructor
    public Food() {
        this("Unknown Food", 0, "Unknown");
    }

    // Inventory/Request constructor
    public Food(String name, int amount, String type) {
        super(name, amount);
        this.type = type;
    }

    // Copy constructor (for inventory copy)
    public Food(Food other) {
        super(other.getName(), other.getAmount());
        this.type = other.type;
    }

    @Override
    public Resource copy() {
        return new Food(this); // uses copy constructor
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return getName() + " (" + type + ") x" + getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        // Check if name from Resource and type from Food match
        return this.getName().equalsIgnoreCase(food.getName()) && this.type.equalsIgnoreCase(food.type);
    }

    @Override
    public int hashCode() {
        return (getName().toLowerCase() + type.toLowerCase()).hashCode();
    }
}
