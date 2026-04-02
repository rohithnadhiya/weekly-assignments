package Week4;
import java.util.*;

class Asset {
    String name;
    double returnRate;

    Asset(String name, double returnRate) {
        this.name = name;
        this.returnRate = returnRate;
    }

    public String toString() {
        return name + ":" + returnRate;
    }
}

public class PortfolioReturnSorting {

    // 🔹 Merge Sort (Stable)
    static void mergeSort(List<Asset> list, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(list, l, m);
            mergeSort(list, m + 1, r);
            merge(list, l, m, r);
        }
    }

    static void merge(List<Asset> list, int l, int m, int r) {
        List<Asset> left = new ArrayList<>(list.subList(l, m + 1));
        List<Asset> right = new ArrayList<>(list.subList(m + 1, r + 1));

        int i = 0, j = 0, k = l;

        while (i < left.size() && j < right.size()) {
            // 🔥 <= ensures STABILITY
            if (left.get(i).returnRate <= right.get(j).returnRate) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) list.set(k++, left.get(i++));
        while (j < right.size()) list.set(k++, right.get(j++));
    }

    // 🔹 Format output
    static String format(List<Asset> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {

        // Sample Input
        List<Asset> assets = new ArrayList<>();
        assets.add(new Asset("A", 5.5));
        assets.add(new Asset("B", 3.2));
        assets.add(new Asset("C", 5.5));

        System.out.println("Input: " + format(assets));

        // Merge Sort
        mergeSort(assets, 0, assets.size() - 1);

        System.out.println("MergeSort (by returnRate): " + format(assets) + " // Stable");
    }
}