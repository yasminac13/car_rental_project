package exceptions;

public class CarNotAvailableException extends RuntimeException {
    public CarNotAvailableException(String message) {
        super(message);
    }

    public CarNotAvailableException(String vehicleId, String details) {
        super(String.format("Vehicle %s is not available: %s", vehicleId, details));
    }
}
