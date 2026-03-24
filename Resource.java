public abstract class Resource implements Comparable<Resource> {

    private String name;
    private int amount;

    // No-arg constructor
    public Resource () {
        this("Unknown", 0);
    }

    // Overloaded constructor
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

    @Override
    public String toString() {
        return "Resource{" + "name=" + name + ", amount=" + amount + '}';
    }

    @Override
    public int compareTo(Resource o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return name != null && name.equalsIgnoreCase(resource.name);
    }

    @Override
    public int hashCode() {
        return (name.toLowerCase()).hashCode();
    }
}
