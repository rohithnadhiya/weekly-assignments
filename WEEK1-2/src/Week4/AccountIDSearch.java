package Week4;
import java.util.Arrays;

public class AccountIDSearch {

    public static void main(String[] args) {

        String[] logs = {"accB", "accA", "accB", "accC"};
        String target = "accB";

        // SORT (required for binary search)
        Arrays.sort(logs);
        System.out.println("Sorted Logs: " + Arrays.toString(logs));

        // LINEAR SEARCH
        int firstL = -1, lastL = -1, compL = 0;

        for (int i = 0; i < logs.length; i++) {
            compL++;
            if (logs[i].equals(target)) {
                if (firstL == -1) firstL = i;
                lastL = i;
            }
        }

        System.out.println("\nLinear Search:");
        System.out.println("First Index = " + firstL);
        System.out.println("Last Index = " + lastL);
        System.out.println("Comparisons = " + compL);

        // BINARY SEARCH
        int low = 0, high = logs.length - 1;
        int firstB = -1, lastB = -1, compB = 0;

        // Find FIRST occurrence
        while (low <= high) {
            compB++;
            int mid = (low + high) / 2;

            if (logs[mid].equals(target)) {
                firstB = mid;
                high = mid - 1;
            } else if (logs[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        // Reset for LAST occurrence
        low = 0;
        high = logs.length - 1;

        while (low <= high) {
            compB++;
            int mid = (low + high) / 2;

            if (logs[mid].equals(target)) {
                lastB = mid;
                low = mid + 1;
            } else if (logs[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        int count = (firstB == -1) ? 0 : (lastB - firstB + 1);

        System.out.println("\nBinary Search:");
        System.out.println("First Index = " + firstB);
        System.out.println("Last Index = " + lastB);
        System.out.println("Count = " + count);
        System.out.println("Comparisons = " + compB);
    }
}