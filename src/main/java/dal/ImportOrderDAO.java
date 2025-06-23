package dal;

import model.ImportOrders;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImportOrderDAO extends DBConnect {

    // ✅ Tạo đơn nhập hàng đầy đủ
    public int createImportOrder(int supplierId, int warehouseId, int accountId, String note,
                                 double totalCost, String code, Timestamp orderDeadline,
                                 Timestamp expectedArrival, Timestamp confirmDate,
                                 String invoiceStatus, String activityNote) {
        String sql = "INSERT INTO import_orders " +
                "(supplier_id, warehouse_id, account_id, note, total_cost, code, order_deadline, " +
                "expected_arrival, confirm_date, invoice_status, activity_note) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, supplierId);
            ps.setInt(2, warehouseId);
            ps.setInt(3, accountId);
            ps.setString(4, note);
            ps.setBigDecimal(5, new java.math.BigDecimal(totalCost));
            ps.setString(6, code);
            ps.setTimestamp(7, orderDeadline);
            ps.setTimestamp(8, expectedArrival);
            ps.setTimestamp(9, confirmDate);
            ps.setString(10, invoiceStatus);
            ps.setString(11, activityNote);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ✅ Lấy danh sách đơn nhập có phân trang và mapping đầy đủ trường
    public List<ImportOrders> getImportOrdersPaginated(String baseQuery, int offset, int limit) {
        List<ImportOrders> list = new ArrayList<>();
        String paginatedQuery = baseQuery + " ORDER BY import_date DESC LIMIT ? OFFSET ?";
        try (PreparedStatement ps = connection.prepareStatement(paginatedQuery)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ImportOrders order = extractImportOrderFromResultSet(rs);
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Lấy toàn bộ danh sách đơn nhập theo câu lệnh SQL truyền vào
    public List<ImportOrders> getImportOrders(String sql) {
        List<ImportOrders> importOrders = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ImportOrders order = extractImportOrderFromResultSet(rs);
                importOrders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return importOrders;
    }

    // ✅ Lấy 1 đơn nhập theo ID
    public ImportOrders getImportOrderById(int importId) {
        String sql = "SELECT * FROM import_orders WHERE import_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, importId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractImportOrderFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Đếm tổng số đơn nhập có điều kiện
    public int countImportOrders(String condition) {
        String sql = "SELECT COUNT(*) FROM import_orders";
        if (condition != null && !condition.trim().isEmpty()) {
            sql += " WHERE " + condition;
        }
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ✅ Cập nhật trạng thái đơn nhập
    public int updateImportStatusToDone(int importId) {
        String sql = "UPDATE import_orders SET status = 'Done' WHERE import_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, importId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int cancelImportOrder(int importId) {
        String sql = "UPDATE import_orders SET status = 'Cancel' WHERE import_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, importId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ✅ Helper method để map ResultSet thành ImportOrders
    private ImportOrders extractImportOrderFromResultSet(ResultSet rs) throws SQLException {
        ImportOrders order = new ImportOrders();
        order.setImportId(rs.getInt("import_id"));
        order.setSupplierId(rs.getInt("supplier_id"));
        order.setWarehouseId(rs.getInt("warehouse_id"));
        order.setAccountId(rs.getInt("account_id"));
        order.setImportDate(rs.getTimestamp("import_date"));
        order.setNote(rs.getString("note"));
        order.setTotalCost(rs.getBigDecimal("total_cost"));
        order.setStatus(rs.getString("status"));
        order.setCode(rs.getString("code"));
        order.setOrderDeadline(rs.getTimestamp("order_deadline"));
        order.setExpectedArrival(rs.getTimestamp("expected_arrival"));
        order.setConfirmDate(rs.getTimestamp("confirm_date"));
        order.setInvoiceStatus(rs.getString("invoice_status"));
        order.setActivityNote(rs.getString("activity_note"));
        return order;
    }

    public int createImportOrder(int supplierId, int warehouseId, int accountId, String note, double totalCost) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return createImportOrder(
                supplierId, warehouseId, accountId, note, totalCost,
                null,     // code
                now, now, now,
                "No",     // invoice_status
                null      // activity_note
        );
    }

}
