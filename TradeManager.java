import java.util.*;

public class TradeManager {

    private Queue<TradeRequest> openRequests = new LinkedList<>();
    private HashMap<String, Colony> colonyMap = new HashMap<>(); // Hashmap: direct access in O(1) time
    // AVLTree: ordered lookup for sorted traversal in O(log n) time
    private AVLTree<Colony> colonyTree = new AVLTree<>();
    private WeightedGraph<Colony> logisticsGraph; // logistics graph for Djikstra lookups

    public void setLogisticsGraph(WeightedGraph<Colony> logisticsGraph) {
        this.logisticsGraph = logisticsGraph;
    }

    public void addColony(Colony colony) { // Add colony through ID string
        colonyMap.put(colony.getId(), colony);
        colonyTree.insert(colony);
    }

    public Colony getColony(String id) { // Find colony in O(1) time
        return colonyMap.get(id);
    }

    public boolean isColonyinSystem(Colony colony) { // Check whether colony exists in system
        return colonyTree.search(colony);
    }

    public void addRequest(TradeRequest trade){
        openRequests.add(trade);
    }

    public void removeColony(Colony colony) {
        colonyMap.remove(colony.getId()); // Remove colony from the hash map
        colonyTree.delete(colony); // Remove colony from the AVL Tree
    }

    public void displayColonies() {
        colonyTree.inorder();
    }

    public boolean matchTrades(){
        if (openRequests.isEmpty()){ return false; }

        // Sort requests by time created (FIFO)
        // Convert open requests to ArrayList for sorting
        ArrayList<TradeRequest> sortedRequests = new ArrayList<>(openRequests);
        SearchSortUtils.mergeSort(sortedRequests);

        TradeRequest matched = null;

        // Iterate through the prioritized list
        java.util.Iterator<TradeRequest> it = sortedRequests.iterator();

        while (it.hasNext()) {
            TradeRequest request = it.next();
            List<Colony> candidates = findCandidates(request);

            if (candidates.isEmpty()) { continue; }

            Colony bestProvider = chooseBestProvider(request, candidates);

            if (bestProvider != null){
                if (executeTrade(request, bestProvider)) {
                    // Remove from the queue/list
                    openRequests.remove(request);
                    return true;
                }
            }
        }
        return false;
    }

    public List<Colony> findCandidates(TradeRequest request){
        List<Colony> eligibleCandidates = new ArrayList<>();

        Colony requester = request.getRequester();
        Resource requestedItem = request.getRequestedResource();
        int amount = request.getRequestedAmt();

        // O(log n) search to find where requester is in sorted list
        Colony foundRequester = colonyMap.get(requester.getId());

        if  (foundRequester == null){ return eligibleCandidates; }

        for (Colony colony : colonyMap.values()) {
            // Skip the requester colony
            if (colony.getId().equals(foundRequester.getId())) {
                continue;
            }

            // Check if the colony has the resource
            if (colony.hasResource(requestedItem.getName(), amount, requestedItem.getClass())) {
                eligibleCandidates.add(colony);
            }
        }
        return eligibleCandidates;
    }

    private Comparator<Colony> colonyScoreComparator(Colony requester){

        // generate the shortest path tree for the requester
        int sourceIndex = logisticsGraph.getIndex(requester);
        WeightedGraph<Colony>.ShortestPathTree pathTree = logisticsGraph.getShortestPath(sourceIndex);

        return (c1, c2) -> {
            double score1 = compatabilityScore(pathTree, c1);
            double score2 = compatabilityScore(pathTree, c2);
            return Double.compare(score2, score1);
        };
    }

    public Colony chooseBestProvider(TradeRequest request, List<Colony> candidates){

        // if candidate list is empty, return null. only time null needs to be returned
        if(candidates == null || candidates.isEmpty()){
            return null;
        }

        candidates.sort(colonyScoreComparator(request.getRequester()));

        return candidates.get(0);
    }

    public boolean executeTrade(TradeRequest trade, Colony bestProvider){

        Colony requester = trade.getRequester();
        Resource template = trade.getRequestedResource();
        int amount = trade.getRequestedAmt();

        // revalidate that provider is still eligible
        if(!bestProvider.hasResource(template.getName(), amount, template.getClass())){
            return false;
        }

        // build a transfer resource object with the right subtype + metadata + amount
        Resource transfer = buildTransferResource(template, amount);

        // update provider inventory (subtract)
        bestProvider.removeResource(transfer.getName(), amount, transfer.getClass());

        // update requester inventory (add)
        requester.addResource(transfer);

        return true;
    }

    private double compatabilityScore(WeightedGraph<Colony>.ShortestPathTree pathTree, Colony provider){
        int destinationIndex = logisticsGraph.getIndex(provider);
        double graphDistance = pathTree.getCost(destinationIndex);

        int risk = provider.getRiskFactor();

        // tunable weights
        double riskWeight = 10.0;
        double distanceWeight = 1.0;

        // convert safety + distance
        double safetyScore = (6 - risk);
        double distanceScore = 1.0 / (1.0 + graphDistance);

        return (riskWeight * safetyScore) + (distanceWeight * distanceScore);
    }


    private Resource buildTransferResource(Resource template, int amount){
        String name = template.getName();

        if (template instanceof Food food) {
            return new Food(name, amount, food.getType());
        }
        if (template instanceof Medicine med) {
            return new Medicine(name, amount, med.getType());
        }
        if (template instanceof Weapon weapon) {
            return new Weapon(name, amount, weapon.getDurability());
        }

        // Fallback: if new resource subclasses appear, and you forget to handle them.
        // You can either throw or return template.copy() and accept amount mismatch risk
        throw new IllegalArgumentException("Unsupported resource subclass: " + template.getClass().getName());
    }

    /** Calculate the absolute minimum road distance required to keep
     every colony in the Wasteland Exchange connected. */
    public void reportMinimumRoadDistance() {
        if (logisticsGraph == null || logisticsGraph.getSize() < 2) {
            System.out.println("Insufficient data to calculate network connectivity.");
            return;
        }

        // Generate the MST starting from the first colony in the graph
        WeightedGraph<Colony>.MST mst = logisticsGraph.getMinimumSpanningTree(0);
        double minDistance = mst.getTotalWeight();

        System.out.println("\n--- INFRASTRUCTURE & CONNECTIVITY REPORT ---");
        System.out.println("To ensure all colonies can trade, a minimum of "
                + String.format("%.2f", minDistance) + " km of road must be secured.");
    }
}
