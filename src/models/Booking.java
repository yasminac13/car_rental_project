package models;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookingId;
    private Client client;
    private Vehicle vehicle;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;
    private BookingStatus status;

    public enum BookingStatus {
        ACTIVE, COMPLETED, CANCELLED
    }

    public Booking(Client client, Vehicle vehicle, LocalDate startDate, LocalDate endDate) {
        this.bookingId = UUID.randomUUID().toString().substring(0, 8);
        this.client = client;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = BookingStatus.ACTIVE;
        this.totalCost = calculateTotalCost();
    }

    private double calculateTotalCost() {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) days = 1;
        return days * vehicle.getPricePerDay();
    }

    public String getBookingId() { return bookingId; }
    public Client getClient() { return client; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getTotalCost() { return totalCost; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public void cancelBooking() {
        this.status = BookingStatus.CANCELLED;
        vehicle.cancelBooking();
    }

    @Override
    public String toString() {
        return String.format("Booking #%s | %s | %s to %s | $%.2f | %s",
                bookingId, vehicle.getBrand() + " " + vehicle.getModel(),
                startDate, endDate, totalCost, status);
    }

}
