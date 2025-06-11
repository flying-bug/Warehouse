package dal;

import java.sql.*;

public class ExportOrderDAO extends DBConnect {

    public int createExportOrder(int customerId, int warehouseId, int accountId, String reason, String note, double totalSalePrice) {
        String sql = "INSERT INTO export_orders (customer_id, warehouse_id, account_id, reason, note, total_sale_price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerId);
            ps.setInt(2, warehouseId);
            ps.setInt(3, accountId);
            ps.setString(4, reason);
            ps.setString(5, note);
            ps.setDouble(6, totalSalePrice);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Trả về export_id
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

}