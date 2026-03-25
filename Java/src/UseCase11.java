import java.util.*;

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        Queue<Reservation> bookingQueue = new LinkedList<>();

        bookingQueue.add(new Reservation("Alice", "Single"));
        bookingQueue.add(new Reservation("Bob", "Single"));
        bookingQueue.add(new Reservation("Charlie", "Single"));
        bookingQueue.add(new Reservation("David", "Double"));
        bookingQueue.add(new Reservation("Eve", "Double"));

        BookingProcessor processor = new BookingProcessor(bookingQueue, inventory);

        Thread t1 = new Thread(processor);
        Thread t2 = new Thread(processor);
        Thread t3 = new Thread(processor);

        t1.start();
        t2.start();
        t3.start();
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

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 2);
        availability.put("Double", 1);
    }

    public synchronized boolean allocateRoom(String roomType) {

        int count = availability.getOrDefault(roomType, 0);

        if (count > 0) {
            availability.put(roomType, count - 1);
            return true;
        }

        return false;
    }
}

class BookingProcessor implements Runnable {

    private Queue<Reservation> queue;
    private RoomInventory inventory;

    public BookingProcessor(Queue<Reservation> queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            Reservation reservation;

            synchronized (queue) {
                if (queue.isEmpty()) {
                    break;
                }
                reservation = queue.poll();
            }

            processReservation(reservation);
        }
    }

    private void processReservation(Reservation reservation) {

        String guest = reservation.getGuestName();
        String roomType = reservation.getRoomType();

        boolean success = inventory.allocateRoom(roomType);

        if (success) {
            System.out.println(Thread.currentThread().getName() +
                    " -> Booking confirmed for " + guest +
                    " (" + roomType + ")");
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " -> No rooms available for " + guest +
                    " (" + roomType + ")");
        }
    }
}