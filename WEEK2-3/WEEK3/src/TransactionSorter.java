import java.util.*;

class Transaction {
    String id;
    double fee;
    String timestamp;

    Transaction(String id, double fee, String timestamp) {
        this.id = id;
        this.fee = fee;
        this.timestamp = timestamp;
    }

    public String toString() {
        return id + ":" + fee + "@" + timestamp;
    }
}

public class TransactionSorter {

    // 🔹 Bubble Sort (by fee)
    static void bubbleSort(List<Transaction> list) {
        int n = list.size();
        int passes = 0, swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            passes++;

            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).fee > list.get(j + 1).fee) {
                    Collections.swap(list, j, j + 1);
                    swaps++;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }

        System.out.println("BubbleSort (fees): " + list);
        System.out.println("// " + passes + " passes, " + swaps + " swaps");
    }

    // 🔹 Insertion Sort (by fee + timestamp)
    static void insertionSort(List<Transaction> list) {
        for (int i = 1; i < list.size(); i++) {
            Transaction key = list.get(i);
            int j = i - 1;

            while (j >= 0 && compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }

        System.out.println("InsertionSort (fee+ts): " + list);
    }

    // 🔹 Compare fee first, then timestamp
    static int compare(Transaction a, Transaction b) {
        if (a.fee != b.fee)
            return Double.compare(a.fee, b.fee);
        return a.timestamp.compareTo(b.timestamp);
    }

    // 🔹 Outliers (>50)
    static void findOutliers(List<Transaction> list) {
        List<Transaction> out = new ArrayList<>();
        for (Transaction t : list) {
            if (t.fee > 50) out.add(t);
        }

        if (out.isEmpty())
            System.out.println("High-fee outliers: none");
        else
            System.out.println("High-fee outliers: " + out);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of transactions: ");
        int n = sc.nextInt();

        List<Transaction> list = new ArrayList<>();

        System.out.println("\nEnter transaction details (id fee timestamp):");
        for (int i = 0; i < n; i++) {
            String id = sc.next();
            double fee = sc.nextDouble();
            String ts = sc.next();
            list.add(new Transaction(id, fee, ts));
        }

        // Copy lists for separate sorting
        List<Transaction> bubbleList = new ArrayList<>(list);
        List<Transaction> insertionList = new ArrayList<>(list);

        bubbleSort(bubbleList);
        System.out.println();

        insertionSort(insertionList);
        System.out.println();

        findOutliers(list);

        sc.close();
    }
}