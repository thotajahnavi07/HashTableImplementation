package src.Week2;

import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> queries = new ArrayList<>();
}

class AutocompleteSystem {

    private TrieNode root;
    private Map<String, Integer> frequencyMap;
    private static final int TOP_K = 10;

    public AutocompleteSystem() {
        root = new TrieNode();
        frequencyMap = new HashMap<>();
    }

    // Insert query into Trie
    public void insert(String query, int frequency) {

        frequencyMap.put(query, frequency);

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            node.queries.add(query);
        }
    }

    // Search suggestions for prefix
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }

            node = node.children.get(c);
        }

        PriorityQueue<String> minHeap = new PriorityQueue<>(
                (a, b) -> frequencyMap.get(a) - frequencyMap.get(b)
        );

        for (String query : node.queries) {

            minHeap.offer(query);

            if (minHeap.size() > TOP_K) {
                minHeap.poll();
            }
        }

        List<String> result = new ArrayList<>();

        while (!minHeap.isEmpty()) {
            result.add(minHeap.poll());
        }

        Collections.reverse(result);
        return result;
    }

    // Update frequency when new search happens
    public void updateFrequency(String query) {

        int freq = frequencyMap.getOrDefault(query, 0) + 1;
        frequencyMap.put(query, freq);

        if (freq == 1) {
            insert(query, freq);
        }
    }

    // Demo
    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.insert("java tutorial", 1234567);
        system.insert("javascript", 987654);
        system.insert("java download", 456789);
        system.insert("java 21 features", 100);

        System.out.println(system.search("jav"));

        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        System.out.println(system.search("jav"));
    }
}
