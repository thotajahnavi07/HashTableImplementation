
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class ParkingSpot {

    enum Status {
        EMPTY,
        OCCUPIED,
        DELETED
    }

    String licensePlate;
    LocalDateTime entryTime;
    Status status;

    ParkingSpot() {
        status = Status.EMPTY;
    }
}

class ParkingLot {

    private ParkingSpot[] table;
    private int capacity;
    private int size;
    private int totalProbes;
    private Map<Integer, Integer> hourlyUsage;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.table = new ParkingSpot[capacity];
        this.hourlyUsage = new HashMap<>();

        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    public String parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (probes < capacity) {

            if (table[index].status == ParkingSpot.Status.EMPTY ||
                    table[index].status == ParkingSpot.Status.DELETED) {

                table[index].licensePlate = licensePlate;
                table[index].entryTime = LocalDateTime.now();
                table[index].status = ParkingSpot.Status.OCCUPIED;

                size++;
                totalProbes += probes;

                int hour = LocalDateTime.now().getHour();
                hourlyUsage.put(hour, hourlyUsage.getOrDefault(hour, 0) + 1);

                return "Assigned spot #" + index + " (" + probes + " probes)";
            }

            index = (index + 1) % capacity;
            probes++;
        }

        return "Parking Full";
    }

    public String exitVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (probes < capacity) {

            if (table[index].status == ParkingSpot.Status.EMPTY) {
                return "Vehicle not found";
            }

            if (table[index].status == ParkingSpot.Status.OCCUPIED &&
                    table[index].licensePlate.equals(licensePlate)) {

                LocalDateTime entry = table[index].entryTime;
                Duration duration = Duration.between(entry, LocalDateTime.now());

                double hours = duration.toMinutes() / 60.0;
                double fee = hours * 5.5;

                table[index].status = ParkingSpot.Status.DELETED;
                size--;

                return "Spot #" + index + " freed, Duration: "
                        + duration.toHours() + "h "
                        + duration.toMinutesPart() + "m, Fee: $"
                        + String.format("%.2f", fee);
            }

            index = (index + 1) % capacity;
            probes++;
        }

        return "Vehicle not found";
    }

    public String getStatistics() {

        double occupancy = ((double) size / capacity) * 100;

        double avgProbes = size == 0 ? 0 : (double) totalProbes / size;

        int peakHour = -1;
        int max = 0;

        for (Map.Entry<Integer, Integer> e : hourlyUsage.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                peakHour = e.getKey();
            }
        }

        return "Occupancy: " + String.format("%.2f", occupancy) + "%, "
                + "Avg Probes: " + String.format("%.2f", avgProbes)
                + ", Peak Hour: " + peakHour + "-" + (peakHour + 1);
    }
}

public class ParkingSystem {

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot(500);

        System.out.println(lot.parkVehicle("ABC-1234"));
        System.out.println(lot.parkVehicle("ABC-1235"));
        System.out.println(lot.parkVehicle("XYZ-9999"));

        System.out.println(lot.exitVehicle("ABC-1234"));

        System.out.println(lot.getStatistics());
    }
}