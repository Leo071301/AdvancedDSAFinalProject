public class Medicine extends Resource {

    private String type;

    public Medicine(String name, int amount, String type){
        super(name, amount);
        this.type = type;
    }

    // Copy constructor
    public Medicine(Medicine other) {
        super(other.getName(), other.getAmount());
    }

    @Override
    public Resource copy() {
        return new Medicine(this);
    }

    public String getType(){ return type; }
}
