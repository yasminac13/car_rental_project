package database;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository {

    private static final String BASE_SELECT =
            "SELECT vehicle_id, vehicle_type, brand, model, year, price_per_day, is_available, " +
                    "transmission, trunk_capacity, drive_type, seating_capacity " +
                    "FROM vehicles ";


    public List<Vehicle> getAllVehicles() {
        String sql = BASE_SELECT + " ORDER BY vehicle_id";

        List<Vehicle> out = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) out.add(mapVehicle(rs));
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error getAllVehicles()", e);
        }
    }

    public List<Vehicle> getAvailableVehicles() {
        String sql = BASE_SELECT + " WHERE is_available = true ORDER BY vehicle_id";

        List<Vehicle> out = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) out.add(mapVehicle(rs));
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error getAvailableVehicles()", e);
        }
    }

    public Vehicle findVehicleById(String vehicleId) {
        String sql = BASE_SELECT + " WHERE vehicle_id = ?";

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, vehicleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapVehicle(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error findVehicleById()", e);
        }
    }

    public void addVehicle(Vehicle v) {
        String sql =
                "INSERT INTO vehicles (" +
                        "vehicle_id, vehicle_type, brand, model, year, price_per_day, is_available, " +
                        "transmission, trunk_capacity, drive_type, seating_capacity" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, v.getVehicleId());
            ps.setString(2, v.getVehicleType());
            ps.setString(3, v.getBrand());
            ps.setString(4, v.getModel());
            ps.setInt(5, v.getYear());
            ps.setDouble(6, v.getPricePerDay());
            ps.setBoolean(7, v.isAvailable());
            ps.setString(8, v.getTransmission());

            if (v instanceof Sedan) {
                Sedan s = (Sedan) v;
                ps.setInt(9, s.getTrunkCapacity());
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.INTEGER);
            } else if (v instanceof SUV) {
                SUV suv = (SUV) v;
                ps.setNull(9, Types.INTEGER);
                ps.setString(10, suv.getDriveType());
                ps.setInt(11, suv.getSeatingCapacity());
            } else {
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.INTEGER);
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error addVehicle()", e);
        }
    }

    public boolean removeVehicle(String vehicleId) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, vehicleId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error removeVehicle()", e);
        }
    }

    public void setAvailability(String vehicleId, boolean available) {
        String sql = "UPDATE vehicles SET is_available = ? WHERE vehicle_id = ?";

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setString(2, vehicleId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error setAvailability()", e);
        }
    }

    private Vehicle mapVehicle(ResultSet rs) throws SQLException {
        String id = rs.getString("vehicle_id");
        String type = rs.getString("vehicle_type");
        String brand = rs.getString("brand");
        String model = rs.getString("model");
        int year = rs.getInt("year");
        double pricePerDay = rs.getDouble("price_per_day");
        boolean available = rs.getBoolean("is_available");
        String transmission = rs.getString("transmission");

        Vehicle v;

        if ("SUV".equalsIgnoreCase(type)) {
            String driveType = rs.getString("drive_type");
            int seats = rs.getInt("seating_capacity");
            v = new SUV(id, brand, model, year, pricePerDay, transmission, driveType, seats);
        } else { // default to Sedan
            int trunk = rs.getInt("trunk_capacity");
            v = new Sedan(id, brand, model, year, pricePerDay, transmission, trunk);
        }

        if (!available) v.book();
        else v.cancelBooking();

        return v;
    }
}
