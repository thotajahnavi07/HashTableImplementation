import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class TokenBucket {

    private final int maxTokens;
    private final double refillRate; // tokens per second
    private double tokens;
    private long lastRefillTime;

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        refill();

        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        double tokensToAdd = (now - lastRefillTime) / 1000.0 * refillRate;

        if (tokensToAdd > 0) {
            tokens = Math.min(maxTokens, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }

    public synchronized int getRemainingTokens() {
        refill();
        return (int) tokens;
    }

    public synchronized long getRetryAfterSeconds() {
        if (tokens >= 1) return 0;
        return (long) (1 / refillRate);
    }

    public int getMaxTokens() {
        return maxTokens;
    }
}

class RateLimitResponse {
    boolean allowed;
    int remaining;
    long retryAfter;

    public RateLimitResponse(boolean allowed, int remaining, long retryAfter) {
        this.allowed = allowed;
        this.remaining = remaining;
        this.retryAfter = retryAfter;
    }

    @Override
    public String toString() {
        if (allowed) {
            return "Allowed (" + remaining + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, retry after " + retryAfter + "s)";
        }
    }
}

public class DistributedRateLimiter {

    // HashMap<clientId, TokenBucket>
    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    private static final int LIMIT = 1000;
    private static final double REFILL_RATE = 1000.0 / 3600.0; // tokens/sec

    public RateLimitResponse checkRateLimit(String clientId) {

        TokenBucket bucket = buckets.computeIfAbsent(
                clientId,
                id -> new TokenBucket(LIMIT, REFILL_RATE)
        );

        boolean allowed = bucket.allowRequest();
        int remaining = bucket.getRemainingTokens();

        if (allowed) {
            return new RateLimitResponse(true, remaining, 0);
        } else {
            long retryAfter = bucket.getRetryAfterSeconds();
            return new RateLimitResponse(false, remaining, retryAfter);
        }
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = buckets.get(clientId);

        if (bucket == null) {
            return "{used:0, limit:" + LIMIT + ", reset:0}";
        }

        int remaining = bucket.getRemainingTokens();
        int used = LIMIT - remaining;
        long reset = System.currentTimeMillis() / 1000 + 3600;

        return "{used:" + used + ", limit:" + LIMIT + ", reset:" + reset + "}";
    }

    public static void main(String[] args) {

        DistributedRateLimiter limiter = new DistributedRateLimiter();

        String clientId = "abc123";

        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.checkRateLimit(clientId));
        }

        System.out.println(limiter.getRateLimitStatus(clientId));
    }
}
