public class Weapon extends Resource{

    private double durability;

    // No-arg constructor
    public Weapon() {
        this("Unknown", 0, 0.0);
    }

    // Inventory/Request constructor
    public Weapon(String name, int amount, double durability){
        super(name, amount);
        this.durability = durability;
    }

    // Copy constructor
    public Weapon (Weapon other) {
        super(other.getName(), other.getAmount());
        this.durability = other.getDurability();
    }

    @Override
    public Resource copy() {
        return new Weapon(this);
    }

    public double getDurability(){ return durability; }

    @Override
    public String toString() {
        return getName() + " (" + durability + ") x" + getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weapon w = (Weapon) o;
        // Compare name and durability
        return this.getName().equalsIgnoreCase(w.getName()) &&
                Double.compare(this.durability, w.durability) == 0;
    }

    @Override
    public int hashCode() {
        int result = getName().toLowerCase().hashCode();
        result = 31 * result + Double.hashCode(durability);
        return result;
    }
}
