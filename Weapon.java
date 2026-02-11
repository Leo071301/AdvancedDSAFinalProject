public class Weapon extends Resource{

    private double durability;

    public Weapon(String name, int amount, double durability){
        super(name, amount);
        this.durability = durability;
    }

    public double getDurability(){ return durability; }
}
