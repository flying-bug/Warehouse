package dal;

import model.Adjustment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdjustmentDAO extends DBConnect {

    // Tìm kiếm theo câu lệnh SQL custom
    public List<Adjustment> searchAdjustments(String sql) {
        List<Adjustment> adjustments = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                adjustments.add(new Adjustment(
                        rs.getInt("adjustment_id"),
                        rs.getInt("product_id"),
                        rs.getInt("warehouse_id"),
                        rs.getInt("account_id"),
                        rs.getString("adjustment_date"),
                        rs.getInt("quantity_change"),
                        rs.getInt("old_quantity"),
                        rs.getString("reason")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return adjustments;
    }

    // Lấy tất cả bản ghi
    public List<Adjustment> getAllAdjustments() {
        List<Adjustment> list = new ArrayList<>();
        String sql = "SELECT * FROM adjustments";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Adjustment(
                        rs.getInt("adjustment_id"),
                        rs.getInt("product_id"),
                        rs.getInt("warehouse_id"),
                        rs.getInt("account_id"),
                        rs.getString("adjustment_date"),
                        rs.getInt("quantity_change"),
                        rs.getInt("old_quantity"),
                        rs.getString("reason")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Thêm bản ghi mới
    public boolean insertAdjustment(Adjustment adjustment) {
        String sql = "INSERT INTO adjustments (product_id, warehouse_id, account_id, quantity_change, old_quantity, reason) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, adjustment.getProductId());
            ps.setInt(2, adjustment.getWarehouseId());
            ps.setInt(3, adjustment.getAccountId());
            ps.setInt(4, adjustment.getQuantityChange());
            ps.setInt(5, adjustment.getOldQuantity());
            ps.setString(6, adjustment.getReason());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Lấy bản ghi theo ID
    public Adjustment getAdjustmentById(int adjustmentId) {
        String sql = "SELECT * FROM adjustments WHERE adjustment_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, adjustmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Adjustment(
                        rs.getInt("adjustment_id"),
                        rs.getInt("product_id"),
                        rs.getInt("warehouse_id"),
                        rs.getInt("account_id"),
                        rs.getString("adjustment_date"),
                        rs.getInt("quantity_change"),
                        rs.getInt("old_quantity"),
                        rs.getString("reason")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Cập nhật bản ghi
    public boolean updateAdjustment(Adjustment adjustment) {
        String sql = "UPDATE adjustments SET product_id=?, warehouse_id=?, account_id=?, adjustment_date=?, quantity_change=?, old_quantity=?, reason=? WHERE adjustment_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, adjustment.getProductId());
            ps.setInt(2, adjustment.getWarehouseId());
            ps.setInt(3, adjustment.getAccountId());
            ps.setString(4, adjustment.getAdjustmentDate());
            ps.setInt(5, adjustment.getQuantityChange());
            ps.setInt(6, adjustment.getOldQuantity());
            ps.setString(7, adjustment.getReason());
            ps.setInt(8, adjustment.getAdjustmentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Xóa bản ghi
    public boolean deleteAdjustment(int adjustmentId) {
        String sql = "DELETE FROM adjustments WHERE adjustment_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, adjustmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
