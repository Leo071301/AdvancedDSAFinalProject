public class Weapon extends Resource{

    private double durability;

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
}
