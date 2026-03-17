import java.util.LinkedList;
import java.util.Queue;

public class UseCase5{

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Double");
        Reservation r3 = new Reservation("Charlie", "Suite");
        Reservation r4 = new Reservation("David", "Single");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);
        bookingQueue.addRequest(r4);

        System.out.println("Booking Request Queue (First-Come-First-Served)");
        System.out.println();

        bookingQueue.displayRequests();
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

class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
    }

    public void displayRequests() {
        for (Reservation r : requestQueue) {
            System.out.println("Guest: " + r.getGuestName());
            System.out.println("Requested Room: " + r.getRoomType());
            System.out.println();
        }
    }
}
