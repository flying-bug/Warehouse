package dal;

import java.sql.*;

public class ExportOrderDAO extends DBConnect {

    public int createExportOrder(int warehouseId, int accountId, String customerName, String customerPhone, String reason, String note, double totalSalePrice) {
        String sql = "INSERT INTO export_orders (warehouse_id, account_id, customer_name, customer_phone, reason, note, total_sale_price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, warehouseId);
            ps.setInt(2, accountId);
            ps.setString(3, customerName);
            ps.setString(4, customerPhone); // 🆕 mới thêm
            ps.setString(5, reason);
            ps.setString(6, note);
            ps.setDouble(7, totalSalePrice);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // export_id
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
