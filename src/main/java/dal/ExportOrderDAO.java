package dal;

import model.ExportOrders;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExportOrderDAO extends DBConnect {

    public int createExportOrder(int customerId, int warehouseId, int accountId, String reason, String note) {
        String sql = "INSERT INTO export_orders (customer_id, warehouse_id, account_id, reason, note) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerId);
            ps.setInt(2, warehouseId);
            ps.setInt(3, accountId);
            ps.setString(4, reason);
            ps.setString(5, note);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public BigDecimal getTotalAmountByExportId(int exportId) {
        String sql = "SELECT SUM(quantity * sale_price * (1 + tax_percent / 100)) AS total " +
                "FROM export_order_details WHERE export_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, exportId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return (total != null) ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public List<ExportOrders> getExportOrder(String sql) {
        List<ExportOrders> exportOrders = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ExportOrders order = mapResultSetToExportOrders(rs);
                order.setTotalAmount(getTotalAmountByExportId(order.getExportId()));
                exportOrders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exportOrders;
    }

    public ExportOrders getExportOrderById(int exportId) {
        String sql = "SELECT * FROM export_orders WHERE export_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, exportId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ExportOrders order = mapResultSetToExportOrders(rs);
                order.setTotalAmount(getTotalAmountByExportId(order.getExportId()));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ExportOrders> getExportOrderPaginated(String baseQuery, int offset, int limit) {
        List<ExportOrders> list = new ArrayList<>();
        String paginatedQuery = baseQuery + " ORDER BY export_date DESC LIMIT ? OFFSET ?";
        try (PreparedStatement ps = connection.prepareStatement(paginatedQuery)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ExportOrders order = mapResultSetToExportOrders(rs);
                order.setTotalAmount(getTotalAmountByExportId(order.getExportId()));
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countExportOrders(String condition) {
        String sql = "SELECT COUNT(*) FROM export_orders";
        if (condition != null && !condition.trim().isEmpty()) {
            sql += " WHERE " + condition;
        }
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int updateExportStatusToDone(int exportId) {
        String sql = "UPDATE export_orders SET export_status = 'Done' WHERE export_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, exportId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int cancelExportOrder(int exportId) {
        String sql = "UPDATE export_orders SET export_status = 'Cancel' WHERE export_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, exportId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private ExportOrders mapResultSetToExportOrders(ResultSet rs) throws SQLException {
        ExportOrders order = new ExportOrders();
        order.setExportId(rs.getInt("export_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setWarehouseId(rs.getInt("warehouse_id"));
        order.setAccountId(rs.getInt("account_id"));
        order.setExportDate(rs.getTimestamp("export_date"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setCode(rs.getString("code"));
        order.setConfirmDate(rs.getTimestamp("confirm_date"));
        order.setExpirationDate(rs.getTimestamp("expiration_date"));
        order.setDueDate(rs.getTimestamp("due_date"));
        order.setReason(rs.getString("reason"));
        order.setNote(rs.getString("note"));
        order.setActivityNote(rs.getString("activity_note"));
        order.setStatus(rs.getString("export_status"));
        order.setInvoiceStatus(rs.getString("invoice_status"));

        Timestamp confirmDate = rs.getTimestamp("created_at");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        long diffMs = confirmDate != null ? now.getTime() - confirmDate.getTime() : 0;
        int remainingDays = (int) (15 - diffMs / (1000 * 60 * 60 * 24));
        order.setPaymentTerms(" " + Math.max(0, remainingDays));

        return order;
    }
}
