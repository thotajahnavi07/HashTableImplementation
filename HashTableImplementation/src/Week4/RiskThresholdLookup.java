import java.util.*;

public class RiskThresholdLookup {

    // 🔹 Linear Search (Unsorted)
    public static void linearSearch(int[] arr, int target) {
        int comparisons = 0;
        boolean found = false;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                System.out.println("Found at index: " + i);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Not found");
        }

        System.out.println("Linear comparisons: " + comparisons);
    }

    // 🔹 Binary Search + Floor + Ceiling
    public static void binarySearch(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int comparisons = 0;

        int floor = -1;
        int ceiling = -1;

        while (low <= high) {
            comparisons++;
            int mid = (low + high) / 2;

            if (arr[mid] == target) {
                System.out.println("\nExact match found at index: " + mid);
                System.out.println("Comparisons: " + comparisons);
                return;
            }

            else if (arr[mid] < target) {
                floor = arr[mid];   // candidate floor
                low = mid + 1;
            }

            else {
                ceiling = arr[mid]; // candidate ceiling
                high = mid - 1;
            }
        }

        // If not found
        System.out.println("\nNot found (Binary Search)");
        System.out.println("Floor (<= target): " + floor);
        System.out.println("Ceiling (>= target): " + ceiling);
        System.out.println("Comparisons: " + comparisons);
        System.out.println("Insertion index: " + low);
    }

    public static void main(String[] args) {

        int[] unsorted = {50, 10, 100, 25};
        int[] sorted = {10, 25, 50, 100};

        int target = 30;

        System.out.println("Unsorted Array: " + Arrays.toString(unsorted));
        System.out.println("Sorted Array: " + Arrays.toString(sorted));

        // Linear Search
        System.out.println("\n--- Linear Search ---");
        linearSearch(unsorted, target);

        // Binary Search
        System.out.println("\n--- Binary Search ---");
        binarySearch(sorted, target);
    }
}