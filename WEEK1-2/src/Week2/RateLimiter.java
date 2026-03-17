package Week2;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

    // Inner TokenBucket class (no separate file needed)
    static class TokenBucket {
        private int maxTokens;
        private double tokens;
        private double refillRate; // tokens per second
        private long lastRefillTime;

        public TokenBucket(int maxTokens, double refillRate) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRate = refillRate;
            this.lastRefillTime = System.nanoTime();
        }

        private void refill() {
            long now = System.nanoTime();
            double seconds = (now - lastRefillTime) / 1e9;

            double tokensToAdd = seconds * refillRate;
            tokens = Math.min(maxTokens, tokens + tokensToAdd);

            lastRefillTime = now;
        }

        public synchronized boolean allowRequest() {
            refill();

            if (tokens >= 1) {
                tokens -= 1;
                return true;
            }
            return false;
        }

        public synchronized int getRemainingTokens() {
            refill();
            return (int) tokens;
        }
    }

    // Map client → token bucket
    private ConcurrentHashMap<String, TokenBucket> clientBuckets;

    public RateLimiter() {
        clientBuckets = new ConcurrentHashMap<>();
    }

    public String checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId,
                new TokenBucket(1000, 1000.0 / 3600)); // 1000/hour

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " remaining)";
        } else {
            return "Denied ❌ Rate limit exceeded. Try later.";
        }
    }

    public String getRateLimitStatus(String clientId) {
        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) return "No data";

        return "Remaining: " + bucket.getRemainingTokens() + "/1000";
    }

    // Main method
    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();
        String client = "abc123";

        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.checkRateLimit(client));
        }

        System.out.println(limiter.getRateLimitStatus(client));
    }
}