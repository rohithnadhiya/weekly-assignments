package Week2;
import java.util.*;

public class TransactionAnalyzer {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time; // in milliseconds

        public Transaction(int id, int amount, String merchant, String account, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    private List<Transaction> transactions;

    public TransactionAnalyzer() {
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // 🔹 Classic Two-Sum
    public List<String> findTwoSum(int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);
                result.add("(" + other.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // 🔹 Two-Sum within 1 hour window
    public List<String> findTwoSumWithTime(int target) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < transactions.size(); i++) {
            for (int j = i + 1; j < transactions.size(); j++) {

                Transaction t1 = transactions.get(i);
                Transaction t2 = transactions.get(j);

                if (Math.abs(t1.time - t2.time) <= 3600000) { // 1 hour
                    if (t1.amount + t2.amount == target) {
                        result.add("(" + t1.id + ", " + t2.id + ")");
                    }
                }
            }
        }

        return result;
    }

    // 🔹 K-Sum (recursive)
    public List<List<Integer>> findKSum(int k, int target) {
        List<List<Integer>> result = new ArrayList<>();
        kSumHelper(0, k, target, new ArrayList<>(), result);
        return result;
    }

    private void kSumHelper(int start, int k, int target,
                            List<Integer> current, List<List<Integer>> result) {

        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        if (k == 0 || start >= transactions.size()) return;

        for (int i = start; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            current.add(t.id);
            kSumHelper(i + 1, k - 1, target - t.amount, current, result);
            current.remove(current.size() - 1);
        }
    }

    // 🔹 Duplicate Detection
    public List<String> detectDuplicates() {
        Map<String, Set<String>> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {
            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new HashSet<>());
            map.get(key).add(t.account);
        }

        for (String key : map.keySet()) {
            if (map.get(key).size() > 1) {
                result.add("Duplicate: " + key + " accounts=" + map.get(key));
            }
        }

        return result;
    }

    // 🔹 Demo
    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        long now = System.currentTimeMillis();

        analyzer.addTransaction(new Transaction(1, 500, "StoreA", "acc1", now));
        analyzer.addTransaction(new Transaction(2, 300, "StoreB", "acc2", now + 1000));
        analyzer.addTransaction(new Transaction(3, 200, "StoreC", "acc3", now + 2000));
        analyzer.addTransaction(new Transaction(4, 500, "StoreA", "acc2", now + 3000));

        System.out.println("Two Sum (500): " + analyzer.findTwoSum(500));

        System.out.println("Two Sum (time window): "
                + analyzer.findTwoSumWithTime(500));

        System.out.println("K Sum (k=3, target=1000): "
                + analyzer.findKSum(3, 1000));

        System.out.println("Duplicates: "
                + analyzer.detectDuplicates());
    }
}