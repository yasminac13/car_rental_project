package database;

import models.Booking;
import models.Client;
import models.Vehicle;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public void addBooking(Booking b) {
        String sql =
                "INSERT INTO bookings " +
                        "(booking_id, client_username, vehicle_id, start_date, end_date, total_cost, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getBookingId());
            ps.setString(2, b.getClient().getUsername());
            ps.setString(3, b.getVehicle().getVehicleId());
            ps.setDate(4, Date.valueOf(b.getStartDate()));
            ps.setDate(5, Date.valueOf(b.getEndDate()));
            ps.setDouble(6, b.getTotalCost());

            ps.setObject(7, b.getStatus().name(), Types.OTHER);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error addBooking(): " + e.getMessage(), e);
        }
    }


            public List<Booking> getAllBookings(ClientRepository clientRepo, VehicleRepository vehicleRepo) {
        String sql =
                "SELECT client_username, vehicle_id, start_date, end_date, status " +
                        "FROM bookings " +
                        "ORDER BY start_date DESC";

        List<Booking> out = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(mapBooking(rs, clientRepo, vehicleRepo));
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error getAllBookings()", e);
        }
    }

    public List<Booking> getClientBookings(Client client, ClientRepository clientRepo, VehicleRepository vehicleRepo) {
        String sql =
                "SELECT client_username, vehicle_id, start_date, end_date, status " +
                        "FROM bookings " +
                        "WHERE client_username = ? " +
                        "ORDER BY start_date DESC";

        List<Booking> out = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, client.getUsername());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapBooking(rs, clientRepo, vehicleRepo));
                }
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error getClientBookings()", e);
        }
    }

    private Booking mapBooking(ResultSet rs, ClientRepository clientRepo, VehicleRepository vehicleRepo) throws SQLException {
        String clientUsername = rs.getString("client_username");
        String vehicleId = rs.getString("vehicle_id");

        Client client = clientRepo.findClientByUsername(clientUsername);
        Vehicle vehicle = vehicleRepo.findVehicleById(vehicleId);

        LocalDate start = rs.getDate("start_date").toLocalDate();
        LocalDate end = rs.getDate("end_date").toLocalDate();

        Booking booking = new Booking(client, vehicle, start, end);

        Booking.BookingStatus status = Booking.BookingStatus.valueOf(rs.getString("status"));
        booking.setStatus(status);

        return booking;
    }
}
