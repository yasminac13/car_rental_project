package database;

import models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDatabase {
    private static RentalDatabase instance;

    //data storage - array list
    private List<Vehicle> vehicles;
    private List<Client> clients;
    private List<Admin> admins;
    private List<Booking> bookings;


    private static final String VEHICLES_FILE = "vehicles.dat";
    private static final String CLIENTS_FILE = "clients.dat";
    private static final String ADMINS_FILE = "admins.dat";
    private static final String BOOKINGS_FILE = "bookings.dat";

    //private constructor for Singleton
    private RentalDatabase() {
        vehicles = new ArrayList<>();
        clients = new ArrayList<>();
        admins = new ArrayList<>();
        bookings = new ArrayList<>();
        loadAllData();
    }

    // Singleton getInstance method -so everyone uses the same database instance
    public static RentalDatabase getInstance() {
        if (instance == null) {
            instance = new RentalDatabase();
        }
        return instance;
    }

    public void initializeSampleData() {
        if (vehicles.isEmpty()) {
            vehicles.add(new Sedan("V001", "Toyota", "Camry", 2023, 50.0, "Automatic", 450));
            vehicles.add(new Sedan("V002", "Honda", "Accord", 2022, 45.0, "Manual", 430));
            vehicles.add(new SUV("V003", "Ford", "Explorer", 2023, 75.0, "Automatic", "AWD", 7));
            vehicles.add(new SUV("V004", "Jeep", "Wrangler", 2022, 80.0, "Manual", "4WD", 5));
            vehicles.add(new Sedan("V005", "BMW", "3 Series", 2024, 90.0, "Automatic", 480));
        }

        if (clients.isEmpty()) {
            clients.add(new Client("john", "pass123", "john@email.com", "John Doe", "DL123456"));
        }

        if (admins.isEmpty()) {
            admins.add(new Admin("admin", "admin123", "admin@rental.com", "Admin User", "EMP001"));
        }

        saveAllData();
    }

    // CRUD operations
    //create
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle); //add to array list
        saveVehicles(); // save to file immediately
    }

    //delete data
    public boolean removeVehicle(String vehicleId) {
        boolean removed = vehicles.removeIf(v -> v.getVehicleId().equals(vehicleId));
        if (removed) {
            saveVehicles();
        }
        return removed;
    }

    //read - getting lists
    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles);//returns copy
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> available = new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);

            if (v.isAvailable()) {
                available.add(v);
            }
        }
        return available;
    }

    //read - find data
    public Vehicle findVehicleById(String vehicleId) {
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            if (v.getVehicleId().equals(vehicleId)) {
                return v;
            }
        }
        return null;
    }

    // Client operations
    public void addClient(Client client) {
        clients.add(client);
        saveClients();
    }

    public Client findClientByUsername(String username) {
        for (Client c : clients) {
            if (c.getUsername().equals(username)) {
                return c;
            }
        }
        return null;
    }

    public List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }

    // Admin operations
    public void addAdmin(Admin admin) {
        admins.add(admin);
        saveAdmins();
    }

    public Admin findAdminByUsername(String username) {
        for (Admin a : admins) {
            if (a.getUsername().equals(username)) {
                return a;
            }
        }
        return null;
    }

    // Booking operations
    public void addBooking(Booking booking) {
        bookings.add(booking);
        saveBookings();
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public List<Booking> getClientBookings(Client client) {
        List<Booking> clientBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getClient().getUsername().equals(client.getUsername())) {
                clientBookings.add(b);
            }
        }
        return clientBookings;
    }

    // Save methods with try-catch-finally (demonstrates exception handling)
    private void saveVehicles() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(VEHICLES_FILE));
            oos.writeObject(vehicles);
        } catch (IOException e) {
            System.err.println("Error saving vehicles: " + e.getMessage());
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    System.err.println("Error closing stream: " + e.getMessage());
                }
            }
        }
    }

    private void saveClients() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CLIENTS_FILE))) {
            oos.writeObject(clients);
        } catch (IOException e) {
            System.err.println("Error saving clients: " + e.getMessage());
        }
    }

    private void saveAdmins() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ADMINS_FILE))) {
            oos.writeObject(admins);
        } catch (IOException e) {
            System.err.println("Error saving admins: " + e.getMessage());
        }
    }

    private void saveBookings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    public void saveAllData() {
        saveVehicles();
        saveClients();
        saveAdmins();
        saveBookings();
    }

    // Load methods with exception handling
    @SuppressWarnings("unchecked")
    private void loadAllData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(VEHICLES_FILE))) {
            vehicles = (List<Vehicle>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No vehicles data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CLIENTS_FILE))) {
            clients = (List<Client>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No clients data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading clients: " + e.getMessage());
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ADMINS_FILE))) {
            admins = (List<Admin>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No admins data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading admins: " + e.getMessage());
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKINGS_FILE))) {
            bookings = (List<Booking>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No bookings data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
    }
}