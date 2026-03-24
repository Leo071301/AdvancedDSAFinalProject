public class Medicine extends Resource {

    private String type;

    // No-arg constructor
    public Medicine() {
        this("Unknown", 0, "Unknown");
    }
    
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine m = (Medicine) o;
        return this.getName().equalsIgnoreCase(m.getName()) && this.type.equalsIgnoreCase(m.getType());
    }

    @Override
    public int hashCode() {
        return (getName().toLowerCase() + type.toLowerCase()).hashCode();
    }
}
