package models;

import java.util.ArrayList;
import java.util.List;

public class Client extends User {
    private static final long serialVersionUID = 1L;
    private String licenseNumber;
    private List<Booking> bookingHistory;

    public Client(String username, String password, String email,
                  String fullName, String licenseNumber) {
        super(username, password, email, fullName);
        this.licenseNumber = licenseNumber;
        this.bookingHistory = new ArrayList<>();
    }

    @Override
    public String getUserType() { return "Client"; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public List<Booking> getBookingHistory() { return bookingHistory; }
    public void addBooking(Booking booking) { this.bookingHistory.add(booking); }

    @Override
    public String toString() {
        return super.toString() + " | License: " + licenseNumber;
    }
}
