public class Weapon extends Resource{

    private double durability;

    public Weapon(String name, int amount, double durability){
        super(name, amount);
        this.durability = durability;
    }

    public Weapon (Weapon other) {
        super(other.getName(), other.getAmount());
    }

    @Override
    public Resource copy() {
        return new Weapon(this);
    }

    public double getDurability(){ return durability; }
}
