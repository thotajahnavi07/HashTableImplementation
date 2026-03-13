
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HashTableImplementation {

    // username -> userId
    private ConcurrentHashMap<String, Integer> usernameMap;

    // username -> number of attempts
    private ConcurrentHashMap<String, Integer> attemptFrequency;

    public HashTableImplementation() {
        usernameMap = new ConcurrentHashMap<>();
        attemptFrequency = new ConcurrentHashMap<>();
    }

    // Check username availability
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        // O(1) lookup
        return !usernameMap.containsKey(username);
    }

    // Register username
    public boolean registerUsername(String username, int userId) {

        if (usernameMap.containsKey(username)) {
            return false;
        }

        usernameMap.put(username, userId);
        return true;
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        // append numbers
        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // replace underscore with dot
        String alt = username.replace("_", ".");
        if (!usernameMap.containsKey(alt)) {
            suggestions.add(alt);
        }

        // add year
        String alt2 = username + "2026";
        if (!usernameMap.containsKey(alt2)) {
            suggestions.add(alt2);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String maxUser = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxUser = entry.getKey();
            }
        }

        return maxUser + " (" + maxCount + " attempts)";
    }

    // Print all registered users
    public void printUsers() {

        System.out.println("Registered Users:");

        for (Map.Entry<String, Integer> entry : usernameMap.entrySet()) {
            System.out.println(entry.getKey() + " -> UserID: " + entry.getValue());
        }
    }

    // Main method for testing
    public static void main(String[] args) {

        HashTableImplementation system = new HashTableImplementation();

        // Pre-register users
        system.registerUsername("john_doe", 1);
        system.registerUsername("admin", 2);
        system.registerUsername("test_user", 3);

        // Check availability
        System.out.println("Check john_doe: " +
                system.checkAvailability("john_doe"));

        System.out.println("Check jane_smith: " +
                system.checkAvailability("jane_smith"));

        // Suggest alternatives
        System.out.println("\nSuggestions for john_doe:");
        List<String> suggestions = system.suggestAlternatives("john_doe");

        for (String s : suggestions) {
            System.out.println(s);
        }

        // Simulate multiple attempts
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        system.checkAvailability("test_user");
        system.checkAvailability("test_user");

        // Get most attempted
        System.out.println("\nMost attempted username: " +
                system.getMostAttempted());

        // Print registered users
        System.out.println();
        system.printUsers();
    }
}