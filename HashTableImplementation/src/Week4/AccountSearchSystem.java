import java.util.*;

public class AccountSearchSystem {

    // 🔹 Linear Search (First & Last Occurrence)
    public static void linearSearch(String[] arr, String target) {
        int first = -1, last = -1, comparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;

            if (arr[i].equals(target)) {
                if (first == -1) first = i;
                last = i;
            }
        }

        System.out.println("\nLinear Search:");
        if (first != -1) {
            System.out.println("First occurrence: " + first);
            System.out.println("Last occurrence: " + last);
        } else {
            System.out.println("Not found");
        }

        System.out.println("Comparisons: " + comparisons);
    }

    // 🔹 Binary Search (Find any occurrence)
    public static int binarySearch(String[] arr, String target, int[] comp) {
        int low = 0, high = arr.length - 1;

        while (low <= high) {
            comp[0]++;
            int mid = (low + high) / 2;

            int cmp = arr[mid].compareTo(target);

            if (cmp == 0) return mid;
            else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }

        return -1;
    }

    // 🔹 Count duplicates using Binary Search
    public static void countOccurrences(String[] arr, String target) {
        int[] comp = {0};

        int index = binarySearch(arr, target, comp);

        if (index == -1) {
            System.out.println("\nBinary Search: Not found");
            System.out.println("Comparisons: " + comp[0]);
            return;
        }

        int count = 1;

        // Left side
        int left = index - 1;
        while (left >= 0 && arr[left].equals(target)) {
            count++;
            left--;
        }

        // Right side
        int right = index + 1;
        while (right < arr.length && arr[right].equals(target)) {
            count++;
            right++;
        }

        System.out.println("\nBinary Search:");
        System.out.println("Found at index: " + index);
        System.out.println("Total occurrences: " + count);
        System.out.println("Comparisons: " + comp[0]);
    }

    public static void main(String[] args) {

        // Sorted array for binary search
        String[] logs = {"accA", "accB", "accB", "accC"};

        System.out.println("Transaction Logs: " + Arrays.toString(logs));

        String target = "accB";

        linearSearch(logs, target);
        countOccurrences(logs, target);
    }
}