public class WeightedEdge extends Edge
        implements Comparable<WeightedEdge> {
    public double weight; // The weight on edge (u, v)

    /** Create a weighted edge on (u, v) */
    public WeightedEdge(int source, int destination, double weight) {
        super(source, destination);
        this.weight = weight;
    }

    @Override // compares weight only (for sorting, MST)
    public int compareTo(WeightedEdge edge) {
        // compares edges by weight:
        // returns < 0 if this.weight < edge.weight
        // returns 0 if equal
        // returns > 0 if this.weight > edge.weight
        return Double.compare(this.weight, edge.weight);
    }

    @Override // compares source, destination, weight (for identity)
    public boolean equals(Object o) {
        // same object in memory → equal
        if (this == o) return true;

        // must be a WeightedEdge to compare
        if (!(o instanceof WeightedEdge)) return false;

        // cast so we can access fields
        WeightedEdge e = (WeightedEdge) o;

        // edges are equal ONLY if source, destination, AND weight match
        return source == e.source &&
                destination == e.destination &&
                Double.compare(weight, e.weight) == 0;
    }

    @Override
    public int hashCode() {
        // start with Edge's hash (source + destination)
        // then include weight to stay consistent with equals()
        return 31 * super.hashCode() + Double.hashCode(weight);
    }
}
