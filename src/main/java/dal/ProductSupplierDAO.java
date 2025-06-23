package dal;

import model.Products;
import model.Suppliers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductSupplierDAO extends DBConnect {

    /**
     * Lấy giá ước tính từ bảng product_suppliers theo productId và supplierId
     * @param productId mã sản phẩm
     * @param supplierId mã nhà cung cấp
     * @return giá nhập (estimated_price), hoặc null nếu không có
     */
    public Double getEstimatedPrice(int productId, int supplierId) {
        String sql = "SELECT estimated_price FROM product_suppliers WHERE product_id = ? AND supplier_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, supplierId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("estimated_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Kiểm tra xem supplier có cung cấp product không
     */
    public boolean supplierProvidesProduct(int productId, int supplierId) {
        String sql = "SELECT 1 FROM product_suppliers WHERE product_id = ? AND supplier_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, supplierId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertProductSupplier(int productId, int supplierId, int duration, double price, String policies) {
        String sql = "INSERT INTO product_suppliers (product_id, supplier_id, delivery_duration, estimated_price, policies) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, supplierId);
            ps.setInt(3, duration);
            ps.setDouble(4, price);
            ps.setString(5, policies);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(int productId, int supplierId) {
        String sql = "SELECT 1 FROM product_suppliers WHERE product_id = ? AND supplier_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, supplierId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // tồn tại thì trả true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // ProductSupplierDAO.java
    public List<Suppliers> getSuppliersByProductId(int productId) {
        List<Suppliers> list = new ArrayList<>();
        String sql = "SELECT s.* FROM product_suppliers ps " +
                "JOIN suppliers s ON ps.supplier_id = s.supplier_id " +
                "WHERE ps.product_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Suppliers supplier = new Suppliers();
                supplier.setSupplierId(rs.getInt("supplier_id"));
                supplier.setName(rs.getString("name"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setEmail(rs.getString("email"));
                supplier.setAddress(rs.getString("address"));
                supplier.setStatus(rs.getInt("status"));
                // add other fields if needed
                list.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Products> getProductsBySupplierId(int supplierId) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT p.* FROM product_suppliers ps " +
                "JOIN products p ON ps.product_id = p.product_id " +
                "WHERE ps.supplier_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Products product = new Products();
                product.setProductId(rs.getInt("product_id"));
                product.setProductCode(rs.getString("product_code"));
                product.setName(rs.getString("name"));
                product.setUnit(rs.getString("unit"));
                product.setStatus(rs.getInt("status"));
                // add thêm các trường nếu cần
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
