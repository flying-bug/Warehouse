package dal;

import model.ExportOrderDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ExportOrderDetailDAO extends DBConnect {

    public boolean insertExportOrderDetail(int exportId, int productId, int quantity, double salePrice) {
        String sql = "INSERT INTO export_order_details (export_id, product_id, quantity, sale_price) VALUES (?, ?, ?, ?)";
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

    public List<ExportOrderDetails> getExportOrderDetails(String sql) {
        List<ExportOrderDetails> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int exportId = rs.getInt("export_id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                BigDecimal salePrice = rs.getBigDecimal("sale_price");
                BigDecimal taxPercent = rs.getBigDecimal("tax_percent"); // ✅ new
                int quantityDelivered = rs.getInt("quantity_delivered"); // ✅ new
                int quantityInvoiced = rs.getInt("quantity_invoiced");   // ✅ new
                String status = rs.getString("export_status");

                ExportOrderDetails detail = new ExportOrderDetails(
                        id,
                        exportId,
                        productId,
                        quantity,
                        salePrice,
                        taxPercent,
                        quantityDelivered,
                        quantityInvoiced,
                        status
                );

                // Tuỳ chọn: gán lineTotal tại đây nếu bạn muốn
                detail.setLineTotal(salePrice.multiply(BigDecimal.valueOf(quantity)));

                list.add(detail);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching export order details", e);
        }
        return list;
    }
}
