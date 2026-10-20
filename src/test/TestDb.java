package test;

import database.RentalDatabaseSql;
import models.*;

import java.util.List;

public class TestDb {

    public static void main(String[] args) {
        RentalDatabaseSql db = new RentalDatabaseSql();

        printHeader("VEHICLES");
        printVehicles(db.getAllVehicles());

        printHeader("CLIENTS");
        printClients(db.getAllClients());

        printHeader("BOOKINGS");
        printBookings(db.getAllBookings());
    }


    private static void printHeader(String title) {
        System.out.println();
        System.out.println("====================================");
        System.out.println(" " + title);
        System.out.println("====================================");
    }

    private static void printVehicles(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.printf("%-6s %-10s %-12s %-10s %-6s %-8s %-12s %-12s%n",
                "ID", "TYPE", "BRAND", "MODEL", "YEAR", "PRICE", "TRANS", "STATUS");
        System.out.println("----------------------------------------------------------------------");

        for (Vehicle v : vehicles) {
            System.out.printf("%-6s %-10s %-12s %-10s %-6d $%-7.2f %-12s %-12s%n",
                    v.getVehicleId(),
                    v.getVehicleType(),
                    v.getBrand(),
                    v.getModel(),
                    v.getYear(),
                    v.getPricePerDay(),
                    v.getTransmission(),
                    v.isAvailable() ? "Available" : "Rented"
            );
        }
    }

    private static void printClients(List<Client> clients) {
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
            return;
        }

        System.out.printf("%-12s %-20s %-25s %-15s%n",
                "USERNAME", "FULL NAME", "EMAIL", "LICENSE");
        System.out.println("-----------------------------------------------------------------------");

        for (Client c : clients) {
            System.out.printf("%-12s %-20s %-25s %-15s%n",
                    c.getUsername(),
                    c.getFullName(),
                    c.getEmail(),
                    c.getLicenseNumber()
            );
        }
    }

    private static void printBookings(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.printf("%-10s %-12s %-8s %-12s %-12s %-10s%n",
                "BOOKING", "CLIENT", "VEHICLE", "FROM", "TO", "STATUS");
        System.out.println("-----------------------------------------------------------------------");

        for (Booking b : bookings) {
            System.out.printf("%-10s %-12s %-8s %-12s %-12s %-10s%n",
                    b.getBookingId(),
                    b.getClient().getUsername(),
                    b.getVehicle().getVehicleId(),
                    b.getStartDate(),
                    b.getEndDate(),
                    b.getStatus()
            );
        }
    }
}
