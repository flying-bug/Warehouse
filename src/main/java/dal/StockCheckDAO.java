package dal;

import model.StockCheck;
import model.StockCheckDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockCheckDAO extends DBConnect {

    // Thêm bản ghi vào bảng stock_checks
    public int insertStockCheck(StockCheck check) {
        String sql = "INSERT INTO stock_checks (warehouse_id, account_id, check_date, end_date, status, note) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, check.getWarehouseId());
            ps.setInt(2, check.getAccountId());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // check_date = NOW()
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // end_date = NOW()
            ps.setString(5, check.getStatus());
            ps.setString(6, check.getNote());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // return check_id
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Thêm từng chi tiết kiểm kê sản phẩm
    public boolean insertStockCheckDetail(StockCheckDetail detail) {
        String sql = "INSERT INTO stock_check_details (check_id, product_id, actual_quantity, system_quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, detail.getCheckId());
            ps.setInt(2, detail.getProductId());
            ps.setInt(3, detail.getActualQuantity());
            ps.setInt(4, detail.getSystemQuantity());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // (Optional) Lấy danh sách kiểm kê đã thực hiện
    public List<StockCheck> getAllStockChecks() {
        List<StockCheck> list = new ArrayList<>();
        String sql = "SELECT * FROM stock_checks ORDER BY check_date DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                StockCheck check = new StockCheck();
                check.setCheckId(rs.getInt("check_id"));
                check.setWarehouseId(rs.getInt("warehouse_id"));
                check.setAccountId(rs.getInt("account_id"));
                check.setCheckDate(rs.getTimestamp("check_date"));
                check.setEndDate(rs.getTimestamp("end_date"));
                check.setStatus(rs.getString("status"));
                check.setNote(rs.getString("note"));
                list.add(check);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getSystemQuantity(int productId, int warehouseId) {
        String sql = "SELECT quantity FROM inventory WHERE product_id = ? AND warehouse_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, warehouseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public List<StockCheckDetail> getDetailsByCheckId(int checkId) {
        List<StockCheckDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM stock_check_details WHERE check_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, checkId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StockCheckDetail detail = new StockCheckDetail();
                    detail.setCheckId(rs.getInt("check_id"));
                    detail.setProductId(rs.getInt("product_id"));
                    detail.setActualQuantity(rs.getInt("actual_quantity"));
                    detail.setSystemQuantity(rs.getInt("system_quantity"));
                    list.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }




}
