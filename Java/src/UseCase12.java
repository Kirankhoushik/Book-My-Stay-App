import java.io.*;
import java.util.*;

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();

        SystemState state = persistenceService.loadState();

        if (state == null) {
            state = new SystemState();
            state.inventory.put("Single", 2);
            state.inventory.put("Double", 1);
            state.inventory.put("Suite", 1);

            state.bookingHistory.add(new Reservation("RES101", "Alice", "Single"));
            state.bookingHistory.add(new Reservation("RES102", "Bob", "Double"));

            System.out.println("Fresh system initialized.\n");
        } else {
            System.out.println("System recovered from file.\n");
        }

        displayState(state);

        persistenceService.saveState(state);

        System.out.println("\nState saved successfully.");
    }

    public static void displayState(SystemState state) {

        System.out.println("Inventory:");
        for (String type : state.inventory.keySet()) {
            System.out.println(type + " -> " + state.inventory.get(type));
        }

        System.out.println("\nBooking History:");
        for (Reservation r : state.bookingHistory) {
            System.out.println(r.getReservationId() + " | " +
                    r.getGuestName() + " | " + r.getRoomType());
        }
    }
}

class SystemState implements Serializable {

    Map<String, Integer> inventory = new HashMap<>();
    List<Reservation> bookingHistory = new ArrayList<>();
}

class Reservation implements Serializable {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class PersistenceService {

    private static final String FILE_NAME = "hotel_state.ser";

    public void saveState(SystemState state) {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    public SystemState loadState() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (SystemState) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error loading state. Starting fresh.");
            return null;
        }
    }
}