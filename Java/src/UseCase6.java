import java.util.*;

public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        Queue<Reservation> bookingQueue = new LinkedList<>();
        bookingQueue.add(new Reservation("Alice", "Single"));
        bookingQueue.add(new Reservation("Bob", "Double"));
        bookingQueue.add(new Reservation("Charlie", "Suite"));
        bookingQueue.add(new Reservation("David", "Single"));

        BookingService bookingService = new BookingService(inventory);

        System.out.println("Processing Booking Requests\n");

        while (!bookingQueue.isEmpty()) {
            Reservation reservation = bookingQueue.poll();
            bookingService.processReservation(reservation);
        }

        System.out.println("\nAllocated Rooms:");
        bookingService.displayAllocations();
    }
}

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) {
        roomAvailability.put(roomType, roomAvailability.get(roomType) - 1);
    }
}

class BookingService {

    private RoomInventory inventory;

    private Set<String> allocatedRoomIds = new HashSet<>();

    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    private int roomCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processReservation(Reservation reservation) {

        String roomType = reservation.getRoomType();

        if (inventory.getAvailability(roomType) > 0) {

            String roomId = generateRoomId(roomType);

            allocatedRoomIds.add(roomId);

            allocatedRooms
                    .computeIfAbsent(roomType, k -> new HashSet<>())
                    .add(roomId);

            inventory.decrementAvailability(roomType);

            System.out.println("Reservation Confirmed for " + reservation.getGuestName());
            System.out.println("Room Type: " + roomType);
            System.out.println("Assigned Room ID: " + roomId + "\n");

        } else {
            System.out.println("No rooms available for " + reservation.getGuestName() +
                    " (" + roomType + ")\n");
        }
    }

    private String generateRoomId(String roomType) {
        String roomId;

        do {
            roomId = roomType.substring(0,1).toUpperCase() + roomCounter++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    public void displayAllocations() {

        for (String roomType : allocatedRooms.keySet()) {
            System.out.println(roomType + " Rooms: " + allocatedRooms.get(roomType));
        }
    }
}