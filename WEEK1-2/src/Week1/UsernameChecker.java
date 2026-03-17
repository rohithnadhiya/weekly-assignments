package Week1;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UsernameChecker {

    // username -> userId (or just existence)
    private ConcurrentHashMap<String, Integer> userMap;

    // username -> attempt count
    private ConcurrentHashMap<String, Integer> attemptCount;

    public UsernameChecker() {
        userMap = new ConcurrentHashMap<>();
        attemptCount = new ConcurrentHashMap<>();
    }

    // Register username (simulate existing users)
    public void registerUser(String username, int userId) {
        userMap.put(username, userId);
    }

    // Check availability in O(1)
    public boolean checkAvailability(String username) {
        // Track attempts
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);

        return !userMap.containsKey(username);
    }

    // Suggest alternatives
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        // Strategy 1: append numbers
        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;
            if (!userMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // Strategy 2: replace "_" with "."
        String modified = username.replace("_", ".");
        if (!userMap.containsKey(modified)) {
            suggestions.add(modified);
        }

        // Strategy 3: random suffix
        String random = username + new Random().nextInt(1000);
        if (!userMap.containsKey(random)) {
            suggestions.add(random);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {
        String maxUser = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : attemptCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxUser = entry.getKey();
            }
        }

        return maxUser + " (" + maxCount + " attempts)";
    }

    // Demo
    public static void main(String[] args) {
        UsernameChecker system = new UsernameChecker();

        // Existing users
        system.registerUser("john_doe", 1);
        system.registerUser("admin", 2);

        // Check availability
        System.out.println(system.checkAvailability("john_doe")); // false
        System.out.println(system.checkAvailability("jane_smith")); // true

        // Suggestions
        System.out.println(system.suggestAlternatives("john_doe"));

        // Simulate attempts
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("john_doe");

        // Most attempted
        System.out.println(system.getMostAttempted());
    }
}