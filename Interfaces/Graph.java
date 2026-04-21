import java.util.List;

public interface Graph<V> {

    /** Return total number of vertices in the graph */
    int getSize();

    /** Return list of all vertices (e.g., Colony objects) */
    List<V> getVertices();

    /** Return the vertex object at a specific index */
    V getVertex(int index);

    /** Return the index of a given vertex object */
    int getIndex(V v);

    /** Return a list of neighbor indices for a given vertex index */
    // Example: if vertex 0 connects to 1 and 2 → returns [1, 2]
    List<Integer> getNeighbors(int index);

    /** Add a new vertex to the graph */
    // Also creates an empty adjacency list for it
    boolean addVertex(V vertex);

    /** Add a weighted edge from vertex u → v */
    // u and v are indices, weight is cost (distance, etc.)
    boolean addEdge(int source, int destination, double weight);

    /** Print all edges in the graph (for testing/debugging) */
    void printEdges();
}
