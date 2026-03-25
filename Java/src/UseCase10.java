import java.util.*;

public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);
        CancellationService cancellationService = new CancellationService(bookingService, inventory);

        bookingService.bookRoom("RES101", "Alice", "Single");
        bookingService.bookRoom("RES102", "Bob", "Double");
        bookingService.bookRoom("RES103", "Charlie", "Suite");

        System.out.println("\n--- Cancelling RES102 ---\n");

        cancellationService.cancelReservation("RES102");

        System.out.println("\n--- Attempt Invalid Cancellation ---\n");

        cancellationService.cancelReservation("RES999");
    }
}

class RoomInventory {

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        availability.put(roomType, availability.get(roomType) - 1);
    }

    public void increment(String roomType) {
        availability.put(roomType, availability.get(roomType) + 1);
    }
}

class BookingService {

    private RoomInventory inventory;
    private Map<String, String> reservationToRoomId = new HashMap<>();
    private Map<String, String> reservationToRoomType = new HashMap<>();

    private int counter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void bookRoom(String reservationId, String guestName, String roomType) {

        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("No rooms available for " + guestName);
            return;
        }

        String roomId = roomType.substring(0,1).toUpperCase() + counter++;

        reservationToRoomId.put(reservationId, roomId);
        reservationToRoomType.put(reservationId, roomType);

        inventory.decrement(roomType);

        System.out.println("Booking Confirmed: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId);
    }

    public String getRoomId(String reservationId) {
        return reservationToRoomId.get(reservationId);
    }

    public String getRoomType(String reservationId) {
        return reservationToRoomType.get(reservationId);
    }

    public void removeReservation(String reservationId) {
        reservationToRoomId.remove(reservationId);
        reservationToRoomType.remove(reservationId);
    }

    public boolean exists(String reservationId) {
        return reservationToRoomId.containsKey(reservationId);
    }
}

class CancellationService {

    private BookingService bookingService;
    private RoomInventory inventory;

    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(BookingService bookingService, RoomInventory inventory) {
        this.bookingService = bookingService;
        this.inventory = inventory;
    }

    public void cancelReservation(String reservationId) {

        if (!bookingService.exists(reservationId)) {
            System.out.println("Cancellation failed: Reservation not found -> " + reservationId);
            return;
        }

        String roomId = bookingService.getRoomId(reservationId);
        String roomType = bookingService.getRoomType(reservationId);

        rollbackStack.push(roomId);

        inventory.increment(roomType);

        bookingService.removeReservation(reservationId);

        System.out.println("Cancellation successful for " + reservationId +
                " | Released Room ID: " + rollbackStack.pop());
    }
}