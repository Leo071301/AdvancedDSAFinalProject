public class Food extends Resource{

    private String type;

    public Food(String name, int amount, String type){
        super(name, amount);
        this.type = type;
    }

    // Copy constructor
    public Food(Food other) {
        super(other.getName(),  other.getAmount());
    }

    @Override
    public Resource copy(){
        return new Food(this);
    }

    public String getType(){ return type; }
}
