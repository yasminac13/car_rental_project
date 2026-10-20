package models;

public class SUV  extends Vehicle{
    private static final long serialVersionUID = 1L;
    private String driveType;
    private int seatingCapacity;

    public SUV(String vehicleId, String brand, String model, int year,
               double pricePerDay, String transmission, String driveType,
               int seatingCapacity) {
        super(vehicleId, brand, model, year, pricePerDay, transmission);
        this.driveType = driveType;
        this.seatingCapacity = seatingCapacity;
    }

    public String getDriveType() { return driveType; }
    public int getSeatingCapacity() { return seatingCapacity; }

    @Override
    public String getVehicleType() { return "SUV"; }

    @Override
    public String toString() {
        return super.toString() +
                String.format(" | %s | %d seats", driveType, seatingCapacity);
    }
}
