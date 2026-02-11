public abstract class Resource {

    private String name;
    private int amount;

    public Resource(String name, int amount){
        this.name = name;
        this.amount = amount;
    }

    public int getAmount(){ return amount; }

    public String getName(){ return name; }
}
