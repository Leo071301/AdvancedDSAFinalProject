public class Medicine extends Resource {

    private String type;

    public Medicine(String name, int amount, String type){
        super(name, amount);
        this.type = type;
    }

    public String getType(){ return type; }
}
