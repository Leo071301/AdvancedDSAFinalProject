import java.util.*;

public class TradeManager {

    private Queue<TradeRequest> openRequests = new LinkedList<>();
    private List<Colony> colonies = new ArrayList<>();

    public void addColony(Colony colony) {
        if (colony == null || colonies.contains(colony)) {
            return;
        }

        int index = 0;
        // Find the first colony that should come after this colony
        while (index < colonies.size() && colonies.get(index).compareTo(colony) < 0) {
            index++;
        }

        // Insert colony at that index
        colonies.add(index, colony);
    }

    public void addRequest(TradeRequest trade){
        openRequests.add(trade);
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
        int requesterIndex = SearchSortUtils.binarySearchById((ArrayList<Colony>) colonies, requester.getId());

        for (int i = 0; i < colonies.size(); i++) {
            // Skip the requester
            if (i == requesterIndex){ continue; }

            Colony colony = colonies.get(i);

            if (colony.hasResource(requestedItem.getName(), amount, requestedItem.getClass())) {
                eligibleCandidates.add(colony);
            }
        }
        return eligibleCandidates;
    }

    private Comparator<Colony> colonyScoreComparator(Colony requester){
        return (c1, c2) -> {
            double score1 = compatabilityScore(requester, c1);
            double score2 = compatabilityScore(requester, c2);
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

    private double compatabilityScore(Colony requester, Colony provider){
        double distance = requester.getLocation().distance(provider.getLocation());
        int risk = provider.getRiskFactor();

        // tunable weights
        double riskWeight = 10.0;
        double distanceWeight = 1.0;

        // convert safety + distance
        double safetyScore = (6 - risk);
        double distanceScore = 1.0 / (1.0 + distance);

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
}
