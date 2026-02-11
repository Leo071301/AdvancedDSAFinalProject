import java.util.Date;

public class TradeRequest implements Comparable<Colony>, Identifiable {
    private final String tr_id; // e.g. TR001
    private final Colony requester; // reference to Colony
    private final String requestedKey;
    private final int requestedAmt;
    private final Date timeCreated =  new Date();
    private boolean isFulfilled = false;
    private static int nextId = 1;

    public TradeRequest(Colony requester, String requestedKey, int requestedAmt) {
        if (requestedAmt < 0) {
            throw new IllegalArgumentException("Requested amount cannot be negative");
        }
        this.tr_id = "TR" + String.format("%03d", nextId++);
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
