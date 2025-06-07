package dal;

import model.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO extends DBConnect {

    // Lấy danh sách tất cả các bản ghi trong bảng inventory
    public List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM inventory";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Tạo đối tượng Inventory từ dữ liệu truy vấn
                Inventory inventory = new Inventory(
                        rs.getInt("product_id"),
                        rs.getInt("warehouse_id"),
                        rs.getInt("quantity")
                );
                inventoryList.add(inventory); // Thêm vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }

    // Thêm mới một bản ghi vào bảng inventory
    public boolean addInventory(Inventory inventory) {
        String sql = "INSERT INTO inventory (product_id, warehouse_id, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, inventory.getProductId());
            ps.setInt(2, inventory.getWarehouseId());
            ps.setInt(3, inventory.getQuantity());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu thêm thất bại
    }

    // Xóa một bản ghi khỏi bảng inventory theo product_id và warehouse_id
    public boolean deleteInventory(int productId, int warehouseId) {
        String sql = "DELETE FROM inventory WHERE product_id = ? AND warehouse_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, warehouseId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu xóa thất bại
    }

    // Cập nhật số lượng trong bảng inventory
    public boolean updateInventory(Inventory inventory) {
        String sql = "UPDATE inventory SET quantity = ? WHERE product_id = ? AND warehouse_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, inventory.getQuantity());
            ps.setInt(2, inventory.getProductId());
            ps.setInt(3, inventory.getWarehouseId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu cập nhật thất bại
    }

    // Lấy thông tin chi tiết của một bản ghi trong bảng inventory theo product_id và warehouse_id
    public Inventory getInventoryById(int productId, int warehouseId) {
        String sql = "SELECT * FROM inventory WHERE product_id = ? AND warehouse_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, warehouseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Tạo đối tượng Inventory từ kết quả truy vấn
                    return new Inventory(
                            rs.getInt("product_id"),
                            rs.getInt("warehouse_id"),
                            rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }
}