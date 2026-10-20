package database;

import models.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRepository {

    public void addAdmin(Admin a) {
        String sql = "INSERT INTO admins (username, password, email, full_name, employee_id) VALUES (?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getUsername());
            ps.setString(2, a.getPassword());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getFullName());
            ps.setString(5, a.getEmployeeId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error addAdmin()", e);
        }
    }

    public Admin findAdminByUsername(String username) {
        String sql = "SELECT username, password, email, full_name, employee_id FROM admins WHERE username = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Admin(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("employee_id")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error findAdminByUsername()", e);
        }
    }
}
