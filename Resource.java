public abstract class Resource {

    private String name;
    private int amount;

    public Resource(String name, int amount){
        this.name = name;
        this.amount = amount;
    }

    public int getAmount(){ return amount; }

    public String getName(){ return name; }

    public abstract Resource copy();

    // Add resources to resource amount
    public void addAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative amount");
        }
        this.amount += amount;
    }

    // Remove resources from resource amount
    public void removeAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot remove negative amount");
        }
        this.amount -= amount;
    }
}
