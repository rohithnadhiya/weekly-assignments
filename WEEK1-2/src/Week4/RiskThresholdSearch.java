package Week4;
import java.util.Arrays;

public class RiskThresholdSearch {

    public static void main(String[] args) {

        int[] risks = {50, 10, 100, 25};
        int target = 30;

        // SORT first
        Arrays.sort(risks);
        System.out.println("Sorted Risks: " + Arrays.toString(risks));

        // LINEAR SEARCH (check exact match)
        int compL = 0;
        boolean found = false;

        for (int i = 0; i < risks.length; i++) {
            compL++;
            if (risks[i] == target) {
                found = true;
                break;
            }
        }

        System.out.println("\nLinear Search:");
        if (found)
            System.out.println("Threshold found");
        else
            System.out.println("Threshold NOT found");
        System.out.println("Comparisons = " + compL);

        // BINARY SEARCH for FLOOR & CEILING
        int low = 0, high = risks.length - 1;
        int floor = -1, ceil = -1;
        int compB = 0;

        while (low <= high) {
            compB++;
            int mid = (low + high) / 2;

            if (risks[mid] == target) {
                floor = ceil = risks[mid];
                break;
            }
            else if (risks[mid] < target) {
                floor = risks[mid];   // possible floor
                low = mid + 1;
            }
            else {
                ceil = risks[mid];    // possible ceiling
                high = mid - 1;
            }
        }

        System.out.println("\nBinary Search:");
        System.out.println("Floor = " + floor);
        System.out.println("Ceiling = " + ceil);
        System.out.println("Comparisons = " + compB);
    }
}