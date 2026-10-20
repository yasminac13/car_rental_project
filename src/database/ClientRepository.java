package database;

import models.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {

    public void addClient(Client c) {
        String sql = "INSERT INTO clients (username, password, email, full_name, license_number) VALUES (?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getUsername());
            ps.setString(2, c.getPassword());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getFullName());
            ps.setString(5, c.getLicenseNumber());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error addClient()", e);
        }
    }

    public Client findClientByUsername(String username) {
        String sql = "SELECT username, password, email, full_name, license_number FROM clients WHERE username = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Client(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("license_number")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error findClientByUsername()", e);
        }
    }

    public List<Client> getAllClients() {
        String sql = "SELECT username, password, email, full_name, license_number FROM clients ORDER BY username";
        List<Client> out = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(new Client(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("license_number")
                ));
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error getAllClients()", e);
        }
    }
}
