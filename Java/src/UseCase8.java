import java.util.*;

public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("RES101", "Alice", "Single"));
        history.addReservation(new Reservation("RES102", "Bob", "Double"));
        history.addReservation(new Reservation("RES103", "Charlie", "Suite"));
        history.addReservation(new Reservation("RES104", "David", "Single"));

        BookingReportService reportService = new BookingReportService();

        System.out.println("Booking History Report");
        System.out.println();

        reportService.displayAllBookings(history);

        System.out.println();
        reportService.displaySummary(history);
    }
}

class Reservation {

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

class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}

class BookingReportService {

    public void displayAllBookings(BookingHistory history) {

        for (Reservation r : history.getReservations()) {
            System.out.println("Reservation ID: " + r.getReservationId());
            System.out.println("Guest: " + r.getGuestName());
            System.out.println("Room Type: " + r.getRoomType());
            System.out.println();
        }
    }

    public void displaySummary(BookingHistory history) {

        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : history.getReservations()) {
            String roomType = r.getRoomType();
            summary.put(roomType, summary.getOrDefault(roomType, 0) + 1);
        }

        System.out.println("Booking Summary:");

        for (String roomType : summary.keySet()) {
            System.out.println(roomType + " Rooms Booked: " + summary.get(roomType));
        }
    }
}