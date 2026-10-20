package gui;

import database.RentalDatabaseSql;
import models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private Admin admin;
    private RentalDatabaseSql db;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        this.db = new RentalDatabaseSql();

        setTitle("Car Rental - Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
    }

    private void createUI() {
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(255, 192, 203));
        JLabel welcomeLabel = new JLabel("Admin Panel - " + admin.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Manage Vehicles", createVehiclePanel());
        tabbedPane.addTab("View Bookings", createBookingsPanel());
        tabbedPane.addTab("View Clients", createClientsPanel());

        JPanel bottomPanel = new JPanel();
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        bottomPanel.add(logoutButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Type", "Brand", "Model", "Year",
                "Price/Day", "Transmission", "Available"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        vehicleTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(vehicleTable);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Vehicle");
        JButton removeButton = new JButton("Remove Vehicle");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> handleAddVehicle());
        removeButton.addActionListener(e -> handleRemoveVehicle());
        refreshButton.addActionListener(e -> loadVehicles());

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadVehicles();
        return panel;
    }

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Booking ID", "Client", "Vehicle",
                "Start Date", "End Date", "Cost", "Status"};
        DefaultTableModel bookingModel = new DefaultTableModel(columns, 0);
        JTable bookingTable = new JTable(bookingModel);

        List<Booking> bookings = db.getAllBookings();
        for (Booking b : bookings) {
            Object[] row = {
                    b.getBookingId(),
                    b.getClient().getFullName(),
                    b.getVehicle().getBrand() + " " + b.getVehicle().getModel(),
                    b.getStartDate(),
                    b.getEndDate(),
                    String.format("$%.2f", b.getTotalCost()),
                    b.getStatus()
            };
            bookingModel.addRow(row);
        }

        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createClientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Username", "Full Name", "Email", "License", "Total Bookings"};
        DefaultTableModel clientModel = new DefaultTableModel(columns, 0);
        JTable clientTable = new JTable(clientModel);

        List<Client> clients = db.getAllClients();
        for (Client c : clients) {
            Object[] row = {
                    c.getUsername(),
                    c.getFullName(),
                    c.getEmail(),
                    c.getLicenseNumber(),
                    db.getClientBookings(c).size()
            };
            clientModel.addRow(row);
        }

        panel.add(new JScrollPane(clientTable), BorderLayout.CENTER);
        return panel;
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

    private void handleAddVehicle() {
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Sedan", "SUV"});
        JTextField idField = new JTextField();
        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox<String> transCombo = new JComboBox<>(new String[]{"Automatic", "Manual"});

        Object[] fields = {
                "Vehicle Type:", typeCombo,
                "Vehicle ID:", idField,
                "Brand:", brandField,
                "Model:", modelField,
                "Year:", yearField,
                "Price per Day:", priceField,
                "Transmission:", transCombo
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Add New Vehicle", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String type = (String) typeCombo.getSelectedItem();
                String id = idField.getText().trim();
                String brand = brandField.getText().trim();
                String model = modelField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                String trans = (String) transCombo.getSelectedItem();

                if (id.isEmpty() || brand.isEmpty() || model.isEmpty()) {
                    throw new IllegalArgumentException("All fields required");
                }

                Vehicle vehicle;
                if ("Sedan".equals(type)) {
                    String capacityStr = JOptionPane.showInputDialog(this, "Trunk Capacity (liters):");
                    int capacity = Integer.parseInt(capacityStr);
                    vehicle = new Sedan(id, brand, model, year, price, trans, capacity);
                } else {
                    String driveType = JOptionPane.showInputDialog(this, "Drive Type (AWD/4WD/2WD):");
                    String seatsStr = JOptionPane.showInputDialog(this, "Seating Capacity:");
                    int seats = Integer.parseInt(seatsStr);
                    vehicle = new SUV(id, brand, model, year, price, trans, driveType, seats);
                }

                db.addVehicle(vehicle);
                loadVehicles();
                JOptionPane.showMessageDialog(this, "Vehicle added successfully!");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemoveVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String vehicleId = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this vehicle?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (db.removeVehicle(vehicleId)) {
                loadVehicles();
                JOptionPane.showMessageDialog(this, "Vehicle removed successfully!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to remove vehicle",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
