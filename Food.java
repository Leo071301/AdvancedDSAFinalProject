public class Food extends Resource {

    private String type; // e.g., "Pills", "Canned", etc.

    // Trade request constructor
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
}
