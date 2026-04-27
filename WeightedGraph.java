import java.util.ArrayList;
import java.util.List;

public class WeightedGraph<V> implements Graph<V> {
    // Stores the actual vertex objects (Colony objects)
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
        System.out.println("\n==========================================");
        System.out.println("ADJACENCY LIST");
        System.out.println("==========================================");

        for (int i = 0; i < neighbors.size(); i++) {
            // We use getName() by casting to Colony,
            // or just use getVertex(i) if you want the full details
            Colony c = (Colony)getVertex(i);
            System.out.println("Node [" + i + "] " + c.getName() + ":");

            for (WeightedEdge edge : neighbors.get(i)) {
                Colony dest = (Colony)getVertex(edge.destination);
                System.out.printf("  --> Connects to: %-10s | Distance: %.2f units\n",
                        dest.getName(), edge.weight);
            }
            System.out.println("------------------------------------------");
        }
    }

    /** Clear the entire graph */
    public void clear() {
        vertices.clear();
        neighbors.clear();
    }

    /** Find single source shortest paths */
    public ShortestPathTree getShortestPath(int sourceVertex) {
        // cost[v] stores the cost of the path from v to the source
        double[] cost = new double[getSize()];
        for (int i = 0; i < cost.length; i++) {
            cost[i] = Double.POSITIVE_INFINITY; // Initial cost set to infinity
        }
        cost[sourceVertex] = 0; // Cost of source is 0

        // parent[v] stores the previous vertex of v in the path
        int[] parent = new int[getSize()];
        parent[sourceVertex] = -1; // The parent of source is set to -1

        // T stores the vertices whose path found so far
        List<Integer> T = new ArrayList<>();

        // Expand T
        while (T.size() < getSize()) {
            // Find smallest cost v in V - T
            int u = -1; // Vertex to be determined
            double currentMinCost = Double.POSITIVE_INFINITY;
            for (int i = 0; i < getSize(); i++) {
                if (!T.contains(i) && cost[i] < currentMinCost) {
                    currentMinCost = cost[i];
                    u = i;
                }
            }

            if (u == -1) break; else T.add(u); // Add a new vertex to T

            // Adjust cost[v] for v that is adjacent to u and v in V - T
            for (WeightedEdge e : neighbors.get(u)) {
                if (!T.contains(e.destination) && cost[e.destination] > cost[u] + e.weight) {
                    cost[e.destination] = cost[u] + e.weight;
                    parent[e.destination] = u;
                }
            }
        } // End of while

        // Create a ShortestPathTree
        return new ShortestPathTree(sourceVertex, parent, T, cost);
    }

    /** Inner class for Shortest Path Results */
    public class ShortestPathTree {
        private int source;
        private int[] parent;
        private List<Integer> searchOrder;
        private double[] cost;

        public ShortestPathTree(int source, int[] parent, List<Integer> searchOrder, double[] cost) {
            this.source = source;
            this.parent = parent;
            this.searchOrder = searchOrder;
            this.cost = cost;
        }

        /** Return the cost for a path from the root to vertex v */
        public double getCost(int v) {
            return cost[v];
        }

        /** Recursively print the path of vertex2 v */
        public void printPath(int v) {
            // Cast to Colony to access getName()
            Colony c = (Colony) vertices.get(v);

            if (parent[v] == -1) {
                System.out.print(c.getName());
            } else {
                printPath(parent[v]);
                System.out.print(" -> " + c.getName());
            }
        }
    }

    /** Get a minimum spanning tree rooted at vertex 0 */
    public MST getMinimumSpanningTree() {
        return getMinimumSpanningTree(0);
    }

    public MST getMinimumSpanningTree(int startingVertex) {
        double[] cost = new double[getSize()];

        // Initialize all costs to infinity
        for (int i = 0; i < cost.length; i++) {
            cost[i] = Double.POSITIVE_INFINITY;
        }

        cost[startingVertex] = 0; // Start here

        int[] parent = new int[getSize()];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = -1;
        }

        double totalWeight = 0;

        List<Integer> T = new ArrayList<>(); // vertices in MST

        while (T.size() < getSize()) {
            int u = -1;
            double currentMinCost = Double.POSITIVE_INFINITY;

            // Find the vertex with smallest cost not yet in T
            for (int i = 0; i < getSize(); i++) {
                if (!T.contains(i) && cost[i] < currentMinCost) {
                    currentMinCost = cost[i];
                    u = i;
                }
            }

            if (u == -1) break; // disconnected graph

            T.add(u);
            totalWeight += cost[u];

            // Update neighbors
            for (WeightedEdge e : neighbors.get(u)) {
                int v = e.destination;

                if (!T.contains(v) && cost[v] > e.weight) {
                    cost[v] = e.weight;
                    parent[v] = u;
                }
            }
        }

        return new MST(startingVertex, parent, totalWeight);
    }

    public class MST {
        private int root;
        private int[] parent;
        private double totalWeight;

        public MST(int root, int[] parent, double totalWeight) {
            this.root = root;
            this.parent = parent;
            this.totalWeight = totalWeight;
        }

        public double getTotalWeight() {
            return totalWeight;
        }

        public int[] getParent() {
            return parent;
        }
    }
}
