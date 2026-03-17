package Week2;
import java.util.*;

public class MultiLevelCacheSystem {

    static class Video {
        String id;
        String data;

        public Video(String id, String data) {
            this.id = id;
            this.data = data;
        }
    }

    // L1 Cache (LRU using LinkedHashMap)
    private LinkedHashMap<String, Video> L1;

    // L2 Cache (simple HashMap)
    private HashMap<String, Video> L2;

    // L3 Database (simulated)
    private HashMap<String, Video> L3;

    // Access count for promotion
    private HashMap<String, Integer> accessCount;

    // Stats
    private int l1Hits = 0, l2Hits = 0, l3Hits = 0;

    public MultiLevelCacheSystem() {

        // L1 with LRU eviction (capacity 3 for demo)
        L1 = new LinkedHashMap<String, Video>(3, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, Video> eldest) {
                return size() > 3;
            }
        };

        L2 = new HashMap<>();
        L3 = new HashMap<>();
        accessCount = new HashMap<>();

        // Preload DB (L3)
        L3.put("video_123", new Video("video_123", "Data123"));
        L3.put("video_999", new Video("video_999", "Data999"));
    }

    // Get video
    public String getVideo(String id) {

        long start = System.nanoTime();

        // L1 check
        if (L1.containsKey(id)) {
            l1Hits++;
            return "L1 HIT ⚡ (" + time(start) + "ms)";
        }

        // L2 check
        if (L2.containsKey(id)) {
            l2Hits++;
            promoteToL1(id, L2.get(id));
            return "L2 HIT ⚡ (" + time(start) + "ms) → Promoted to L1";
        }

        // L3 (DB)
        if (L3.containsKey(id)) {
            l3Hits++;

            Video v = L3.get(id);
            L2.put(id, v);
            accessCount.put(id, 1);

            return "L3 HIT 🐢 (" + time(start) + "ms) → Added to L2";
        }

        return "Video not found";
    }

    // Promote to L1
    private void promoteToL1(String id, Video v) {
        accessCount.put(id, accessCount.getOrDefault(id, 0) + 1);

        if (accessCount.get(id) >= 2) {
            L1.put(id, v);
        }
    }

    // Time helper
    private String time(long start) {
        return String.format("%.2f", (System.nanoTime() - start) / 1e6);
    }

    // Stats
    public void getStats() {
        int total = l1Hits + l2Hits + l3Hits;

        System.out.println("\n📊 Cache Stats:");
        System.out.println("L1 Hit Rate: " + percent(l1Hits, total));
        System.out.println("L2 Hit Rate: " + percent(l2Hits, total));
        System.out.println("L3 Hit Rate: " + percent(l3Hits, total));
    }

    private String percent(int part, int total) {
        if (total == 0) return "0%";
        return String.format("%.2f%%", (part * 100.0 / total));
    }

    // Demo
    public static void main(String[] args) {

        MultiLevelCacheSystem system = new MultiLevelCacheSystem();

        System.out.println(system.getVideo("video_123")); // L3 → L2
        System.out.println(system.getVideo("video_123")); // L2 → L1
        System.out.println(system.getVideo("video_123")); // L1 HIT

        System.out.println(system.getVideo("video_999")); // L3 → L2

        system.getStats();
    }
}