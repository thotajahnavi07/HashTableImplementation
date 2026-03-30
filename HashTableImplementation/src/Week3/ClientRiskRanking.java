import java.util.*;

// Client Class
class Client {
    String name;
    int riskScore;
    double accountBalance;

    public Client(String name, int riskScore, double accountBalance) {
        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    public String toString() {
        return name + ":" + riskScore + " (Bal:" + accountBalance + ")";
    }
}

public class ClientRiskRanking {

    // 🔹 Bubble Sort (Ascending riskScore)
    public static void bubbleSort(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {

                    // Swap
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break; // Optimization
        }

        System.out.println("\nBubble Sort (ASC): " + Arrays.toString(arr));
        System.out.println("Swaps: " + swaps);
    }

    // 🔹 Insertion Sort (DESC riskScore + accountBalance)
    public static void insertionSort(Client[] arr) {
        for (int i = 1; i < arr.length; i++) {

            Client key = arr[i];
            int j = i - 1;

            // Sort by risk DESC, then balance ASC
            while (j >= 0 &&
                    (arr[j].riskScore < key.riskScore ||
                            (arr[j].riskScore == key.riskScore &&
                                    arr[j].accountBalance > key.accountBalance))) {

                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }

        System.out.println("\nInsertion Sort (DESC risk + Balance): " + Arrays.toString(arr));
    }

    // 🔹 Top 10 High Risk Clients
    public static void topHighRisk(Client[] arr) {
        System.out.println("\nTop High Risk Clients:");

        int limit = Math.min(10, arr.length);

        for (int i = 0; i < limit; i++) {
            System.out.println(arr[i]);
        }
    }

    public static void main(String[] args) {

        Client[] clients = {
                new Client("ClientA", 20, 5000),
                new Client("ClientB", 50, 2000),
                new Client("ClientC", 80, 10000),
                new Client("ClientD", 50, 3000)
        };

        // Copy arrays
        Client[] bubbleArr = Arrays.copyOf(clients, clients.length);
        Client[] insertionArr = Arrays.copyOf(clients, clients.length);

        bubbleSort(bubbleArr);
        insertionSort(insertionArr);
        topHighRisk(insertionArr);
    }
}