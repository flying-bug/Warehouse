package dal;

import java.sql.*;

public class ExportOrderDetailDAO extends DBConnect {

    public boolean insertExportOrderDetail(int exportId, int productId, int quantity, double salePrice) {
        String sql = "INSERT INTO export_order_details (export_id, product_id, quantity, sale_price) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, exportId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, salePrice);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
