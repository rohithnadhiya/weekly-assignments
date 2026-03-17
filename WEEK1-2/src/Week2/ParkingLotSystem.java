package Week2;
import java.util.*;

public class ParkingLotSystem {

    static class ParkingSpot {
        String licensePlate;
        long entryTime;
        boolean occupied;

        ParkingSpot() {
            this.licensePlate = null;
            this.entryTime = 0;
            this.occupied = false;
        }
    }

    private ParkingSpot[] table;
    private int capacity;
    private int size;
    private int totalProbes;

    public ParkingLotSystem(int capacity) {
        this.capacity = capacity;
        this.table = new ParkingSpot[capacity];
        this.size = 0;
        this.totalProbes = 0;

        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle (linear probing)
    public String parkVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        size++;
        totalProbes += probes;

        return "Assigned spot #" + index + " (" + probes + " probes)";
    }

    // Exit vehicle
    public String exitVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            if (table[index].licensePlate.equals(plate)) {

                long durationMillis = System.currentTimeMillis() - table[index].entryTime;
                double hours = durationMillis / (1000.0 * 60 * 60);

                double fee = Math.ceil(hours * 5); // $5 per hour

                table[index].occupied = false;
                table[index].licensePlate = null;

                size--;

                return "Spot #" + index + " freed, Duration: "
                        + String.format("%.2f", hours)
                        + " hrs, Fee: $" + fee;
            }

            index = (index + 1) % capacity;
            probes++;

            if (probes > capacity) break;
        }

        return "Vehicle not found";
    }

    // Find nearest available spot
    public int findNearestSpot() {
        for (int i = 0; i < capacity; i++) {
            if (!table[i].occupied) {
                return i;
            }
        }
        return -1;
    }

    // Statistics
    public String getStatistics() {
        double occupancy = (size * 100.0) / capacity;
        double avgProbes = (size == 0) ? 0 : (totalProbes * 1.0 / size);

        return "Occupancy: " + String.format("%.2f", occupancy) + "%, "
                + "Avg Probes: " + String.format("%.2f", avgProbes);
    }

    // Demo
    public static void main(String[] args) throws InterruptedException {

        ParkingLotSystem system = new ParkingLotSystem(10);

        System.out.println(system.parkVehicle("ABC-1234"));
        System.out.println(system.parkVehicle("ABC-1235"));
        System.out.println(system.parkVehicle("XYZ-9999"));

        Thread.sleep(2000); // simulate time

        System.out.println(system.exitVehicle("ABC-1234"));

        System.out.println("Nearest Spot: " + system.findNearestSpot());

        System.out.println(system.getStatistics());
    }
}