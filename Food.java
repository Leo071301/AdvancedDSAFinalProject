public class Food extends Resource{

    private String type;

    public Food(String name, int amount, String type){
        super(name, amount);
        this.type = type;
    }

    public String getType(){ return type; }
}
