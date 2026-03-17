package Week1;
import java.util.*;
import java.util.concurrent.*;

public class RealTimeAnalytics {

    // page -> total visits
    private ConcurrentHashMap<String, Integer> pageViews;

    // page -> unique users
    private ConcurrentHashMap<String, Set<String>> uniqueUsers;

    // source -> count
    private ConcurrentHashMap<String, Integer> sourceCount;

    public RealTimeAnalytics() {
        pageViews = new ConcurrentHashMap<>();
        uniqueUsers = new ConcurrentHashMap<>();
        sourceCount = new ConcurrentHashMap<>();
    }

    // Process event
    public void processEvent(String url, String userId, String source) {

        // Page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Unique users
        uniqueUsers.putIfAbsent(url, ConcurrentHashMap.newKeySet());
        uniqueUsers.get(url).add(userId);

        // Traffic source
        sourceCount.put(source, sourceCount.getOrDefault(source, 0) + 1);
    }

    // Get Top N pages
    public List<String> getTopPages(int N) {
        PriorityQueue<Map.Entry<String, Integer>> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {
            minHeap.offer(entry);
            if (minHeap.size() > N) {
                minHeap.poll();
            }
        }

        List<String> result = new ArrayList<>();

        while (!minHeap.isEmpty()) {
            Map.Entry<String, Integer> entry = minHeap.poll();
            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueUsers.get(page).size();

            result.add(page + " - " + views + " views (" + unique + " unique)");
        }

        Collections.reverse(result); // highest first
        return result;
    }

    // Traffic source stats
    public String getSourceStats() {
        int total = sourceCount.values().stream().mapToInt(i -> i).sum();

        StringBuilder sb = new StringBuilder("Traffic Sources:\n");

        for (Map.Entry<String, Integer> entry : sourceCount.entrySet()) {
            double percent = (entry.getValue() * 100.0) / total;
            sb.append(entry.getKey())
                    .append(": ")
                    .append(String.format("%.1f", percent))
                    .append("%\n");
        }

        return sb.toString();
    }

    // Demo
    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent("/article/breaking-news", "user_123", "google");
        analytics.processEvent("/article/breaking-news", "user_456", "facebook");
        analytics.processEvent("/sports/championship", "user_789", "direct");
        analytics.processEvent("/article/breaking-news", "user_123", "google");

        System.out.println("Top Pages:");
        for (String page : analytics.getTopPages(2)) {
            System.out.println(page);
        }

        System.out.println("\n" + analytics.getSourceStats());
    }
}