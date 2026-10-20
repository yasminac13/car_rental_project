package gui;

import database.RentalDatabaseSql;
import models.*;
import exceptions.CarNotAvailableException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ClientDashboard extends JFrame {
    private Client client;
    private RentalDatabaseSql db;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;

    public ClientDashboard(Client client) {
        this.client = client;
        this.db = new RentalDatabaseSql();

        setTitle("Car Rental - Client Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        loadVehicles();
    }

    private void createUI() {
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180));
        JLabel welcomeLabel = new JLabel("Welcome, " + client.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel);

        String[] columns = {"ID", "Type", "Brand", "Model", "Year",
                "Price/Day", "Transmission", "Available"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        vehicleTable = new JTable(tableModel);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(vehicleTable);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton rentButton = new JButton("Rent Selected Vehicle");
        JButton myBookingsButton = new JButton("My Bookings");
        JButton refreshButton = new JButton("Refresh List");
        JButton logoutButton = new JButton("Logout");

        rentButton.addActionListener(e -> handleRent());
        myBookingsButton.addActionListener(e -> showMyBookings());
        refreshButton.addActionListener(e -> loadVehicles());
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        buttonPanel.add(rentButton);
        buttonPanel.add(myBookingsButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadVehicles() {
        tableModel.setRowCount(0);
        List<Vehicle> vehicles = db.getAllVehicles();

        for (Vehicle v : vehicles) {
            Object[] row = {
                    v.getVehicleId(),
                    v.getVehicleType(),
                    v.getBrand(),
                    v.getModel(),
                    v.getYear(),
                    String.format("$%.2f", v.getPricePerDay()),
                    v.getTransmission(),
                    v.isAvailable() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }

    private void handleRent() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String vehicleId = (String) tableModel.getValueAt(selectedRow, 0);
        Vehicle vehicle = db.findVehicleById(vehicleId);

        try {
            if (vehicle == null) throw new IllegalArgumentException("Vehicle not found");
            if (!vehicle.isAvailable()) throw new CarNotAvailableException(vehicleId, "Already rented");

            JTextField startDateField = new JTextField(LocalDate.now().toString());
            JTextField endDateField = new JTextField(LocalDate.now().plusDays(3).toString());

            Object[] fields = {
                    "Start Date (YYYY-MM-DD):", startDateField,
                    "End Date (YYYY-MM-DD):", endDateField,
                    "Vehicle:", vehicle.toString(),
                    "Price per day:", String.format("$%.2f", vehicle.getPricePerDay())
            };

            int result = JOptionPane.showConfirmDialog(this, fields,
                    "Rent Vehicle", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                LocalDate startDate = LocalDate.parse(startDateField.getText());
                LocalDate endDate = LocalDate.parse(endDateField.getText());

                if (endDate.isBefore(startDate)) {
                    throw new IllegalArgumentException("End date must be after start date");
                }

                Booking booking = new Booking(client, vehicle, startDate, endDate);
                db.addBooking(booking);

                JOptionPane.showMessageDialog(this,
                        String.format("Booking successful!\nBooking ID: %s\nTotal Cost: $%.2f",
                                booking.getBookingId(), booking.getTotalCost()),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                loadVehicles();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMyBookings() {
        List<Booking> bookings = db.getClientBookings(client);

        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "You have no bookings yet",
                    "My Bookings", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columns = {"Booking ID", "Vehicle", "Start Date", "End Date", "Cost", "Status"};
        DefaultTableModel bookingModel = new DefaultTableModel(columns, 0);

        for (Booking b : bookings) {
            Object[] row = {
                    b.getBookingId(),
                    b.getVehicle().getBrand() + " " + b.getVehicle().getModel(),
                    b.getStartDate(),
                    b.getEndDate(),
                    String.format("$%.2f", b.getTotalCost()),
                    b.getStatus()
            };
            bookingModel.addRow(row);
        }

        JTable bookingTable = new JTable(bookingModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        JOptionPane.showMessageDialog(this, scrollPane,
                "My Bookings", JOptionPane.INFORMATION_MESSAGE);
    }
}
