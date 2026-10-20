package models;

public class Sedan extends Vehicle {
    private static final long serialVersionUID = 1L;
    private int trunkCapacity;

    public Sedan(String vehicleId, String brand, String model, int year,
                 double pricePerDay, String transmission, int trunkCapacity) {
        super(vehicleId, brand, model, year, pricePerDay, transmission);
        this.trunkCapacity = trunkCapacity;
    }

    public int getTrunkCapacity() { return trunkCapacity; }

    @Override
    public String getVehicleType() { return "Sedan"; }

    @Override
    public String toString() {
        return super.toString() + " | Trunk: " + trunkCapacity + "L";
    }
}
