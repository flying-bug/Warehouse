package dal;

import model.Roles;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO extends DBConnect {

    public List<Roles> getAllRoles() {
        List<Roles> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Tạo đối tượng Roles cho mỗi bản ghi
                Roles role = new Roles(
                        rs.getInt("role_id"),
                        rs.getString("role_name")
                );
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    // Lấy thông tin một role dựa trên ID
    public Roles getRoleById(int roleId) {
        String sql = "SELECT * FROM roles WHERE role_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    return new Roles(
                            rs.getInt("role_id"),
                            rs.getString("role_name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}