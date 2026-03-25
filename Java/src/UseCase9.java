import java.util.HashMap;
import java.util.Map;

public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        BookingService bookingService = new BookingService(inventory);

        try {
            bookingService.bookRoom("Alice", "Single");
            bookingService.bookRoom("Bob", "Double");
            bookingService.bookRoom("Charlie", "InvalidType");
            bookingService.bookRoom("David", "Suite");
            bookingService.bookRoom("Eve", "Suite");
            bookingService.bookRoom("Frank", "Suite");
        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nSystem still running safely...");
    }
}

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 2);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, -1);
    }

    public void reduceRoom(String roomType) throws InvalidBookingException {

        if (!availability.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int count = availability.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        availability.put(roomType, count - 1);
    }
}

class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void bookRoom(String guestName, String roomType) throws InvalidBookingException {

        validateRoomType(roomType);

        inventory.reduceRoom(roomType);

        System.out.println("Booking confirmed for " + guestName + " (" + roomType + ")");
    }

    private void validateRoomType(String roomType) throws InvalidBookingException {

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        if (!roomType.equals("Single") &&
                !roomType.equals("Double") &&
                !roomType.equals("Suite")) {

            throw new InvalidBookingException("Unsupported room type: " + roomType);
        }
    }
}