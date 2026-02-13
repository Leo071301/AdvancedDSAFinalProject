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
                executeTrade(request, bestProvider);
                // stop (only one match per call)
                break;
            }
        }
        // if no request matched, exit without any changes
        return;

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

            }
        }


        return eligibleCandidates;
    }

    public Colony chooseBestProvider(TradeRequest request, List<Colony> candidates){

        // if candidate list is empty, return null. only time null needs to be returned

        // initialize variable to track bestProvider and highestScore

        // for each candidate:
            // calculate compatabilityScore (risk weighted heavier than distance)
            // track highest score (if this.score > other.score, then max is this.score)

        // return colony with the highest compatability score

        return null;
    }



    public void executeTrade(TradeRequest trade, Colony bestProvider){

        // revalidate that provider is still eligible

        // update provider inventory (subtract)

        // update requester inventory (add)

        // remove trade from openRequests


    }


}
