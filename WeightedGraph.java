import java.util.ArrayList;
import java.util.List;

public class WeightedGraph<V> implements Graph<V> {
    // Stores the actual vertex objects (for your project: Colony objects)
    protected List<V> vertices = new ArrayList<>();

    // Adjacency list: each vertex index has a list of weighted edges
    protected List<List<WeightedEdge>> neighbors = new ArrayList<>();

    /** Construct an empty weighted graph */
    public WeightedGraph() {
    }

    @Override
    public int getSize() {
        return vertices.size();
    }

    @Override
    public List<V> getVertices() {
        return vertices;
    }

    @Override
    public V getVertex(int index) {
        return vertices.get(index);
    }

    @Override
    public int getIndex(V v) {
        return vertices.indexOf(v);
    }

    @Override
    public List<Integer> getNeighbors(int index) {
        List<Integer> result = new ArrayList<>();

        // Go through all edges leaving this vertex
        for (WeightedEdge edge : neighbors.get(index)) {
            result.add(edge.destination);
        }

        return result;
    }

    @Override
    public boolean addVertex(V vertex) {
        // Prevent duplicate vertices
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);

            // Create an empty adjacency list for the new vertex
            neighbors.add(new ArrayList<>());

            return true;
        }

        return false;
    }

    /**
     * Add an undirected weighted edge between u and v
     * This means:
     * u -> v
     * v -> u
     */
    @Override
    public boolean addEdge(int u, int v, double weight) {
        boolean addedForward = addEdge(new WeightedEdge(u, v, weight));
        boolean addedBackward = addEdge(new WeightedEdge(v, u, weight));

        return addedForward && addedBackward;
    }

    /** Add one weighted edge object directly */
    public boolean addEdge(WeightedEdge edge) {
        // Make sure source index exists
        if (edge.source < 0 || edge.source >= getSize()) {
            throw new IllegalArgumentException("No such index: " + edge.source);
        }

        // Make sure destination index exists
        if (edge.destination < 0 || edge.destination >= getSize()) {
            throw new IllegalArgumentException("No such index: " + edge.destination);
        }

        // Prevent duplicate exact edges
        if (!neighbors.get(edge.source).contains(edge)) {
            neighbors.get(edge.source).add(edge);
            return true;
        }

        return false;
    }

    /** Return the degree (number of connected neighbors) of a vertex */
    public int getDegree(int v) {
        return neighbors.get(v).size();
    }

    /** Return the weight on the edge (u, v) */
    public double getWeight(int u, int v) {
        for (WeightedEdge edge : neighbors.get(u)) {
            if (edge.destination == v) {
                return edge.weight;
            }
        }

        throw new IllegalArgumentException("Edge does not exist.");
    }

    @Override
    public void printEdges() {
        for (int u = 0; u < neighbors.size(); u++) {
            System.out.print(getVertex(u) + " (" + u + "): ");

            for (WeightedEdge edge : neighbors.get(u)) {
                System.out.print("(" + edge.source + ", " +
                        edge.destination + ", " + edge.weight + ") ");
            }

            System.out.println();
        }
    }

    /** Clear the entire graph */
    public void clear() {
        vertices.clear();
        neighbors.clear();
    }
}
