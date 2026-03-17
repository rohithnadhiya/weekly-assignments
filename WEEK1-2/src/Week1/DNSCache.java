package Week1;
import java.util.*;
import java.util.concurrent.*;

public class DNSCache {

    class DNSEntry {
        String domain;
        String ip;
        long expiryTime;

        DNSEntry(String domain, String ip, long ttlMillis) {
            this.domain = domain;
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + ttlMillis;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final int capacity;
    private final Map<String, DNSEntry> cache;

    private int hits = 0;
    private int misses = 0;

    public DNSCache(int capacity) {
        this.capacity = capacity;

        // LRU Cache using LinkedHashMap
        this.cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };

        startCleanupThread();
    }

    // Resolve domain
    public synchronized String resolve(String domain) {
        DNSEntry entry = cache.get(domain);

        // Cache HIT
        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT → " + entry.ip;
        }

        // Cache MISS or expired
        misses++;

        String ip = queryUpstreamDNS(domain);

        // Store with TTL (e.g., 5 seconds)
        cache.put(domain, new DNSEntry(domain, ip, 5000));

        return "Cache MISS → " + ip;
    }

    // Simulate DNS lookup
    private String queryUpstreamDNS(String domain) {
        return "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);
    }

    // Background cleanup
    private void startCleanupThread() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            synchronized (this) {
                Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
                while (it.hasNext()) {
                    if (it.next().getValue().isExpired()) {
                        it.remove();
                    }
                }
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    // Stats
    public String getStats() {
        int total = hits + misses;
        double hitRate = (total == 0) ? 0 : (hits * 100.0 / total);

        return "Hit Rate: " + String.format("%.2f", hitRate) + "%";
    }

    // Demo
    public static void main(String[] args) throws InterruptedException {
        DNSCache dns = new DNSCache(3);

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com")); // HIT

        Thread.sleep(6000); // wait for TTL expiry

        System.out.println(dns.resolve("google.com")); // MISS again

        System.out.println(dns.getStats());
    }
}