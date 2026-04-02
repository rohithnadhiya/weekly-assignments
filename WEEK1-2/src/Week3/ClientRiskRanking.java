package Week3;
import java.util.*;

class Client {
    String name;
    int riskScore;

    Client(String name, int riskScore) {
        this.name = name;
        this.riskScore = riskScore;
    }

    public String toString() {
        return name + ":" + riskScore;
    }
}

public class ClientRiskRanking {

    // Bubble Sort (ascending)
    static void bubbleSort(List<Client> list) {
        int swaps = 0;

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).riskScore > list.get(j + 1).riskScore) {
                    Collections.swap(list, j, j + 1);
                    swaps++;
                }
            }
        }

        System.out.println("Bubble (asc): " + format(list) + " // Swaps: " + swaps);
    }

    // Insertion Sort (descending)
    static void insertionSort(List<Client> list) {
        for (int i = 1; i < list.size(); i++) {
            Client key = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).riskScore < key.riskScore) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }

        System.out.println("Insertion (desc): " + format(list));
    }

    // Top 3 risks
    static void topClients(List<Client> list) {
        System.out.print("Top 3 risks: ");
        for (int i = 0; i < Math.min(3, list.size()); i++) {
            System.out.print(list.get(i).name + "(" + list.get(i).riskScore + ")");
            if (i < 2 && i < list.size() - 1) System.out.print(", ");
        }
        System.out.println();
    }

    // Format output
    static String format(List<Client> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {

        // Sample Input (as given)
        List<Client> clients = new ArrayList<>();
        clients.add(new Client("C", 80));
        clients.add(new Client("A", 20));
        clients.add(new Client("B", 50));

        System.out.println("Input: " + format(clients));

        // Bubble Sort
        List<Client> bubbleList = new ArrayList<>(clients);
        bubbleSort(bubbleList);

        // Insertion Sort
        List<Client> insertionList = new ArrayList<>(clients);
        insertionSort(insertionList);

        // Top 3
        topClients(insertionList);
    }
}