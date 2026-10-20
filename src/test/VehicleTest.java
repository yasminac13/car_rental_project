package test;

import models.Sedan;
import models.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {

    @Test
    void vehicleShouldStartAvailable() {
        Vehicle v = new Sedan("V1", "Toyota", "Corolla", 2020, 50.0, "Automatic", 450);
        assertTrue(v.isAvailable(), "New vehicles should be available by default");
    }

    @Test
    void bookShouldMakeVehicleUnavailable() {
        Vehicle v = new Sedan("V2", "Honda", "Civic", 2021, 60.0, "Manual", 400);

        v.book();

        assertFalse(v.isAvailable(), "After booking, vehicle should become unavailable");
    }

    @Test
    void cancelBookingShouldMakeVehicleAvailableAgain() {
        Vehicle v = new Sedan("V3", "BMW", "320i", 2022, 120.0, "Automatic", 480);

        v.book();
        assertFalse(v.isAvailable());

        v.cancelBooking();
        assertTrue(v.isAvailable(), "After cancelBooking, vehicle should become available again");
    }
}

