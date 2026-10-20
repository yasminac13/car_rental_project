package database;

import models.*;

import java.util.List;

public class RentalDatabaseSql {

    private final VehicleRepository vehicleRepo = new VehicleRepository();
    private final ClientRepository clientRepo = new ClientRepository();
    private final AdminRepository adminRepo = new AdminRepository();
    private final BookingRepository bookingRepo = new BookingRepository();

    public void addVehicle(Vehicle v) { vehicleRepo.addVehicle(v); }
    public boolean removeVehicle(String vehicleId) { return vehicleRepo.removeVehicle(vehicleId); }
    public List<Vehicle> getAllVehicles() { return vehicleRepo.getAllVehicles(); }
    public List<Vehicle> getAvailableVehicles() { return vehicleRepo.getAvailableVehicles(); }
    public Vehicle findVehicleById(String vehicleId) { return vehicleRepo.findVehicleById(vehicleId); }

    public void addClient(Client c) { clientRepo.addClient(c); }
    public Client findClientByUsername(String username) { return clientRepo.findClientByUsername(username); }
    public List<Client> getAllClients() { return clientRepo.getAllClients(); }

    public void addAdmin(Admin a) { adminRepo.addAdmin(a); }
    public Admin findAdminByUsername(String username) { return adminRepo.findAdminByUsername(username); }

    public void addBooking(Booking b) {
        bookingRepo.addBooking(b);
        vehicleRepo.setAvailability(b.getVehicle().getVehicleId(), false);
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.getAllBookings(clientRepo, vehicleRepo);
    }

    public List<Booking> getClientBookings(Client c) {
        return bookingRepo.getClientBookings(c, clientRepo, vehicleRepo);
    }
}
