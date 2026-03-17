package Week2;
import java.util.*;

public class AutocompleteSystem {

    // Store query -> frequency
    private Map<String, Integer> freqMap;

    // Trie Node
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<String> words = new ArrayList<>(); // store words with this prefix
    }

    private TrieNode root;

    public AutocompleteSystem() {
        freqMap = new HashMap<>();
        root = new TrieNode();
    }

    // Insert query into Trie
    private void insert(String word) {
        TrieNode node = root;

        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            // store word for prefix
            node.words.add(word);
        }
    }

    // Add or update frequency
    public void addQuery(String query) {
        freqMap.put(query, freqMap.getOrDefault(query, 0) + 1);
        insert(query);
    }

    // Get suggestions for prefix
    public List<String> search(String prefix) {
        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        // Min-heap for top 10 results
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> freqMap.get(a) - freqMap.get(b)
        );

        for (String word : node.words) {
            pq.offer(word);
            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        Collections.reverse(result); // highest freq first
        return result;
    }

    // Demo
    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        // Add queries
        system.addQuery("java tutorial");
        system.addQuery("java tutorial");
        system.addQuery("java stream");
        system.addQuery("java spring");
        system.addQuery("javascript basics");
        system.addQuery("java download");
        system.addQuery("java download");
        system.addQuery("java download");

        // Search
        System.out.println("Suggestions for 'jav':");
        List<String> results = system.search("jav");

        int rank = 1;
        for (String res : results) {
            System.out.println(rank + ". " + res +
                    " (" + system.freqMap.get(res) + " searches)");
            rank++;
        }
    }
}