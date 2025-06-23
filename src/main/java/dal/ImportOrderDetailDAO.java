package dal;

import model.ImportOrderDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImportOrderDetailDAO extends DBConnect {

    /**
     * ✅ Thêm chi tiết đơn nhập (hỗ trợ đầy đủ các trường mới)
     */
    public boolean insertImportDetail(int importId, int productId, int quantity, double costPrice,
                                      double taxPercent, int quantityReceived, int quantityInvoiced) {
        String sql = "INSERT INTO import_order_details " +
                "(import_id, product_id, quantity, cost_price, tax_percent, quantity_received, quantity_invoiced) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, importId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setBigDecimal(4, new java.math.BigDecimal(costPrice));
            ps.setBigDecimal(5, new java.math.BigDecimal(taxPercent));
            ps.setInt(6, quantityReceived);
            ps.setInt(7, quantityInvoiced);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ✅ Lấy danh sách chi tiết đơn nhập (tùy theo câu lệnh truyền vào)
     */
    public List<ImportOrderDetails> getImportOrderDetails(String sql) {
        List<ImportOrderDetails> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ImportOrderDetails detail = new ImportOrderDetails();
                detail.setId(rs.getInt("id"));
                detail.setImportId(rs.getInt("import_id"));
                detail.setProductId(rs.getInt("product_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setCostPrice(rs.getBigDecimal("cost_price"));
                detail.setTaxPercent(rs.getBigDecimal("tax_percent"));
                detail.setQuantityReceived(rs.getInt("quantity_received"));
                detail.setQuantityInvoiced(rs.getInt("quantity_invoiced"));
                detail.setImportStatus(rs.getString("import_status"));

                list.add(detail);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching import order details", e);
        }
        return list;
    }

    /**
     * ✅ Cập nhật số lượng đã nhận thực tế
     */
    public boolean updateReceivedQuantity(int detailId, int newReceivedQty) {
        String sql = "UPDATE import_order_details SET quantity_received = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newReceivedQty);
            ps.setInt(2, detailId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ✅ Cập nhật trạng thái từng dòng chi tiết
     */
    public boolean updateDetailStatus(int detailId, String status) {
        String sql = "UPDATE import_order_details SET import_status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, detailId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertImportDetail(int importId, int productId, int quantity, double costPrice) {
        return insertImportDetail(importId, productId, quantity, costPrice, 0.0, 0, 0);
    }

}
