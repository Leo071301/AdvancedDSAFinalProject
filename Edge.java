public class Edge {
    int source;
    int destination;

    public Edge(int source, int destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        // If both references point to the same object in memory → they are equal
        if (this == o) return true;

        // If the object is null or not an Edge → not equal
        if (o == null || getClass() != o.getClass()) return false;

        // Cast the object to Edge so we can access its fields
        Edge edge = (Edge) o;

        // Two edges are equal if they connect the same source → destination
        return source == edge.source && destination == edge.destination;
    }

    @Override
    public int hashCode() {
        // Generates a hash based on source and destination
        // 31 is a common multiplier to reduce collisions
        return 31 * source + destination;
    }
}
