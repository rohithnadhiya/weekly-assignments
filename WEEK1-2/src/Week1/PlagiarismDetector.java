package Week1;
import java.util.*;

public class PlagiarismDetector {

    private Map<String, Set<String>> index; // ngram -> docIds
    private int N = 5; // n-gram size

    public PlagiarismDetector() {
        index = new HashMap<>();
    }

    // Generate n-grams
    private List<String> generateNGrams(String text) {
        List<String> ngrams = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            ngrams.add(sb.toString().trim());
        }
        return ngrams;
    }

    // Add document to index
    public void addDocument(String docId, String content) {
        List<String> ngrams = generateNGrams(content);

        for (String gram : ngrams) {
            index.putIfAbsent(gram, new HashSet<>());
            index.get(gram).add(docId);
        }
    }

    // Analyze document
    public void analyzeDocument(String docId, String content) {
        List<String> ngrams = generateNGrams(content);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {
            if (index.containsKey(gram)) {
                for (String existingDoc : index.get(gram)) {
                    if (!existingDoc.equals(docId)) {
                        matchCount.put(existingDoc,
                                matchCount.getOrDefault(existingDoc, 0) + 1);
                    }
                }
            }
        }

        // Print results
        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {
            int matches = entry.getValue();
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Matched with " + entry.getKey());
            System.out.println("Similarity: " + String.format("%.2f", similarity) + "%");

            if (similarity > 50) {
                System.out.println("⚠️ PLAGIARISM DETECTED");
            } else if (similarity > 15) {
                System.out.println("⚠️ Suspicious");
            }
            System.out.println();
        }
    }

    // Demo
    public static void main(String[] args) {
        PlagiarismDetector detector = new PlagiarismDetector();

        String doc1 = "data structures and algorithms are important in computer science";
        String doc2 = "data structures and algorithms are very important for programming";
        String doc3 = "machine learning is a different field of study";

        detector.addDocument("doc1", doc1);
        detector.addDocument("doc2", doc2);

        System.out.println("Analyzing doc3:");
        detector.analyzeDocument("doc3", doc3);

        System.out.println("Analyzing similar doc:");
        detector.analyzeDocument("doc_new",
                "data structures and algorithms are important in programming");
    }
}