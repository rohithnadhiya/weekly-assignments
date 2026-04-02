package Week3;
import java.util.*;

public class TradeVolumeAnalysis {

    // 🔹 Merge Sort (Ascending)
    static void mergeSort(int[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    static void merge(int[] arr, int l, int m, int r) {
        int[] left = Arrays.copyOfRange(arr, l, m + 1);
        int[] right = Arrays.copyOfRange(arr, m + 1, r + 1);

        int i = 0, j = 0, k = l;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) { // stable
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }

        while (i < left.length) arr[k++] = left[i++];
        while (j < right.length) arr[k++] = right[j++];
    }

    // 🔹 Quick Sort (Descending)
    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    static int partition(int[] arr, int low, int high) {
        int mid = (low + high) / 2;
        int pivot = arr[mid]; // median pivot

        int i = low, j = high;

        while (i <= j) {
            while (arr[i] > pivot) i++;      // DESC
            while (arr[j] < pivot) j--;

            if (i <= j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        return i - 1;
    }

    // 🔹 Print array
    static String format(int[] arr) {
        return Arrays.toString(arr);
    }

    public static void main(String[] args) {

        // Sample Input
        int[] trades = {500, 100, 300};

        System.out.println("Input: [trade3:500, trade1:100, trade2:300]");

        // Merge Sort
        int[] mergeArr = trades.clone();
        mergeSort(mergeArr, 0, mergeArr.length - 1);
        System.out.println("MergeSort: " + format(mergeArr) + " // Stable");

        // Quick Sort
        int[] quickArr = trades.clone();
        quickSort(quickArr, 0, quickArr.length - 1);
        System.out.println("QuickSort (desc): " + format(quickArr) + " // Pivot: median");

        // Total
        int sum = 0;
        for (int x : trades) sum += x;
        System.out.println("Merged morning-afternoon total: " + sum);
    }
}