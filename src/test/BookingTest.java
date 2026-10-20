package test;

import models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    @Test
    void totalCostShouldBeDaysTimesPricePerDay() {
        Client c = new Client("john", "pass", "john@test.com", "John Doe", "LIC123");
        Vehicle v = new Sedan("V10", "Toyota", "Yaris", 2019, 50.0, "Automatic", 350);

        // 2026-01-01 to 2026-01-04 = 3 days
        Booking b = new Booking(c, v,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 4));

        assertEquals(150.0, b.getTotalCost(), 0.0001, "3 days * 50.0 should be 150.0");
    }

    @Test
    void cancelBookingShouldSetCancelledAndMakeVehicleAvailable() {
        Client c = new Client("maria", "pass", "maria@test.com", "Maria Ionescu", "LIC999");
        Vehicle v = new Sedan("V12", "Ford", "Focus", 2018, 40.0, "Manual", 420);

        // Vehicle is booked, then booking is cancelled
        v.book();
        assertFalse(v.isAvailable());

        Booking b = new Booking(c, v,
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 5));

        b.cancelBooking();

        assertEquals(Booking.BookingStatus.CANCELLED, b.getStatus(), "Booking should become CANCELLED");
        assertTrue(v.isAvailable(), "Vehicle should become available after cancelling booking");
    }
}

