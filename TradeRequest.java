import java.util.Date;

public class TradeRequest implements Comparable<TradeRequest>, Identifiable {
    private final String tr_id; // e.g. TR001
    private final Colony requester; // reference to Colony who made trade request
    private Resource requestedResource;
    private final int requestedAmt;
    private final Date timeCreated =  new Date(); // time when the trade request is created
    private boolean isFulfilled = false;
    private static int nextId = 1;

    public TradeRequest(Colony requester, Resource requestedResource) {
        if (requestedResource.getAmount() < 0) {
            throw new IllegalArgumentException("Requested amount cannot be negative");
        }
        this.tr_id = "TR" + String.format("%03d", nextId++); // generate trade request's unique id when created
        this.requester = requester;
        this.requestedResource = requestedResource.copy();
        this.requestedAmt = requestedResource.getAmount();
    }

    @Override
    public String getId() {
        return tr_id;
    }

    public Colony getRequester() {
        return requester;
    }

    public Resource getRequestedResource() {
        return requestedResource;
    }

    public int getRequestedAmt() {
        return requestedAmt;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public boolean isFulfilled() {
        return isFulfilled;
    }

    // TODO: create the function that determines trade request sortability
    @Override
    public int compareTo(TradeRequest o) {
        return 0;
    }

    @Override
    public String toString() {
        return requester.getName() + "'s Trade Request:\n"
                + "Trade ID: " + tr_id + "\n"
                + "Resource Wanted: " + requestedResource.toString() + "\n"
                + "Date Created: " + timeCreated + "\n";
    }
}
