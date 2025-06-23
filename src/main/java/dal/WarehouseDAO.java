package dal;

import model.Warehouses;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseDAO extends DBConnect {

    // Lấy danh sách tất cả các kho từ cơ sở dữ liệu
    public List<Warehouses> getAllWarehouses() {
        List<Warehouses> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM warehouses"; // Câu lệnh SQL để lấy toàn bộ kho

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Warehouses warehouse = new Warehouses(
                        rs.getInt("warehouse_id"),
                        rs.getString("name"),
                        rs.getString("location")
                );
                warehouses.add(warehouse); // Thêm vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouses;
    }

    // Thêm mới một kho vào cơ sở dữ liệu
    public boolean addWarehouse(Warehouses warehouse) {
        String sql = "INSERT INTO warehouses (name, location) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, warehouse.getName());
            ps.setString(2, warehouse.getLocation());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi
    }

    // Xóa kho theo ID
    public boolean deleteWarehouse(int warehouseId) {
        String sql = "DELETE FROM warehouses WHERE warehouse_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, warehouseId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi
    }

    // Cập nhật thông tin kho
    public boolean updateWarehouse(Warehouses warehouse) {
        String sql = "UPDATE warehouses SET name = ?, location = ? WHERE warehouse_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, warehouse.getName());
            ps.setString(2, warehouse.getLocation());
            ps.setInt(3, warehouse.getWarehouseId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi
    }

    // Lấy thông tin chi tiết của một kho theo ID
    public Warehouses getWarehouseById(int warehouseId) {
        String sql = "SELECT * FROM warehouses WHERE warehouse_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, warehouseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Trả về đối tượng Warehouses
                    return new Warehouses(
                            rs.getInt("warehouse_id"),
                            rs.getString("name"),
                            rs.getString("location")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }

    // Kiểm tra xem tên kho đã tồn tại hay chưa (case-insensitive)
    public boolean isWarehouseNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM warehouses WHERE LOWER(name) = LOWER(?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Trả về Map<warehouse_id, Warehouses>
    public Map<Integer, Warehouses> getWarehouseMap() {
        Map<Integer, Warehouses> map = new HashMap<>();
        String sql = "SELECT * FROM warehouses";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Warehouses warehouse = new Warehouses(
                        rs.getInt("warehouse_id"),
                        rs.getString("name"),
                        rs.getString("location")
                );
                map.put(warehouse.getWarehouseId(), warehouse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

}