import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TradeManager {

    List<TradeRequest> openRequests = new ArrayList<>();

    List<Colony> colonies = new ArrayList<>();

    public void addRequest(TradeRequest trade){
        openRequests.add(trade);
    }

    public void matchTrades(){

        // use TradeRequest.compareTo() to sort open requests by timestamp (+ urgency maybe)
        Collections.sort(openRequests);

        TradeRequest matched = null;

        // iterate through openRequests in priority order
        // for each request:
        for(TradeRequest request : openRequests){
            // call findCandidates(request)
            List<Colony> candidates = findCandidates(request);
            // if candidates list is empty -> continue to the next request
            if(candidates.isEmpty()){
                continue; // skip current request
            }
            // call chooseBestProvider(request, candidates)
            Colony bestProvider = chooseBestProvider(request, candidates);
            // if best provider is found:
            if(bestProvider != null){
                // call executeTrade(request, bestProvider)
                boolean executed = executeTrade(request, bestProvider);

                if(executed){
                    matched = request;
                    break; // stop (only one match per call)
                }

                // otherwise keep looping to try the next request
            }
        }

        if(matched != null){
            openRequests.remove(matched);
        }
        // if no request matched, exit without any changes


    }

    public List<Colony> findCandidates(TradeRequest request){

        List<Colony> eligibleCandidates = new ArrayList<>();

        Colony requested = request.getRequester();
        Resource requestedItem = request.getRequestedResource();
        int amount = request.getRequestedAmt();

        // for each colony:
        for(Colony colony : colonies){
            // if colony is not the requestor
            if(colony == requested){
                continue;
            }

            // if colony inventory contains requested item
            if(colony.hasResource(requestedItem, amount)){
                eligibleCandidates.add(colony);

            }
        }
            // if colonyAmount >= requestAmount
            // if colonyAmount - requestAmount >= colonyMinimum
                // add to eligible candidates


        return eligibleCandidates;
    }

    public Colony chooseBestProvider(TradeRequest request, List<Colony> candidates){

        // if candidate list is empty, return null. only time null needs to be returned
        if(candidates == null || candidates.isEmpty()){
            return null;
        }

        Colony requester = request.getRequester();

        // initialize variable to track bestProvider and highestScore
        Colony bestProvider = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        // for each candidate:
        for(Colony candidate : candidates){
            // calculate compatabilityScore (risk weighted heavier than distance)
            double score = compatabilityScore(requester, candidate);

            // track highest score (if this.score > other.score, then max is this.score)
            if(score > bestScore){
                bestScore = score;
                bestProvider = candidate;
            }
        }



        // return colony with the highest compatability score

        return bestProvider;
    }



    public boolean executeTrade(TradeRequest trade, Colony bestProvider){

        Colony requestor = trade.getRequester();
        Resource template = trade.getRequestedResource();
        int amount = trade.getRequestedAmt();

        // revalidate that provider is still eligible
        if(!bestProvider.hasResource(template, amount)){
            return false;
        }

        // build a transfer resource object with the right subtype + metadata + amount I guess
        Resource transfer = buildTransferResource(template, amount);


        // update provider inventory (subtract)
        bestProvider.removeResource(transfer);

        // update requester inventory (add)
        requestor.addResource(transfer);

        // remove trade from openRequests (done in matcheTrade())

        return true;


    }

    private double compatabilityScore(Colony requestor, Colony provider){
        double distance = requestor.getLocation().distance(provider.getLocation());
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

        // Fallback: if new resoure subclasses appear and you forget to handle them.
        // You can either throw or return template.copy() and accept amount mismatch risk
        throw new IllegalArgumentException("Unsupported resource subclass: " + template.getClass().getName());

    }


}
