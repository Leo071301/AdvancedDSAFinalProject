import java.util.Date;

public class TradeRequest implements Comparable<Colony>, Identifiable {
    private final String tr_id; // e.g. TR001
    private final Colony requester; // reference to Colony
    private final String requestedKey;
    private final int requestedAmt;
    private final Date timeCreated =  new Date();
    private boolean isFulfilled = false;

    public TradeRequest(String tr_id, Colony requester, String requestedKey, int requestedAmt) {
        if (requestedAmt < 0) {
            throw new IllegalArgumentException("Requested amount cannot be negative");
        }
        if (tr_id == null) {
            throw new IllegalArgumentException("Trade requester id cannot be null");
        }
        this.tr_id = tr_id;
        this.requester = requester;
        this.requestedKey = requestedKey;
        this.requestedAmt = requestedAmt;
    }

    @Override
    public String getId() {
        return tr_id;
    }

    public Colony getRequester() {
        return requester;
    }

    public String getRequestedKey() {
        return requestedKey;
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

    // TODO: create the function that determines trade request matchability
    @Override
    public int compareTo(Colony o) { return 0; }
}
