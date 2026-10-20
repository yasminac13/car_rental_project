package models;
import interfaces.Bookable;
import java.io.Serializable;

public abstract class Vehicle implements Serializable, Bookable {
    private static final long serialVersionUID = 1L;

    private String vehicleId;
    private String brand;
    private String model;
    private int year;
    private double pricePerDay;
    private boolean isAvailable;
    private String transmission;

    public Vehicle(String vehicleId, String brand, String model, int year,
                   double pricePerDay, String transmission) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.pricePerDay = pricePerDay;
        this.isAvailable = true;
        this.transmission = transmission;
    }

    public abstract String getVehicleType();

    public String getVehicleId() { return vehicleId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }
    public String getTransmission() { return transmission; }

    @Override
    public boolean isAvailable() { return isAvailable; }

    @Override
    public void book() { this.isAvailable = false; }

    @Override
    public void cancelBooking() { this.isAvailable = true; }

    @Override
    public String toString() {
        return String.format("%s %s %s (%d) - $%.2f/day [%s] - %s",
                getVehicleType(), brand, model, year, pricePerDay,
                transmission, isAvailable ? "Available" : "Rented");
    }


}
