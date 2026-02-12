public class Medicine extends Resource {

    private String type;

    // Inventory/Request constructor
    public Medicine(String name, int amount, String type){
        super(name, amount);
        this.type = type;
    }

    // Copy constructor
    public Medicine(Medicine other) {
        super(other.getName(), other.getAmount());
        this.type = other.getType();
    }

    @Override
    public Resource copy() {
        return new Medicine(this);
    }

    public String getType(){ return type; }

    @Override
    public String toString() {
        return getName() + " (" + type + ") x" + getAmount();
    }
}
