package dal;

import model.Products;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DBConnect {

    // Get a list of all products
    public List<Products> getAllProducts() {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Products product = new Products(
                        rs.getInt("product_id"),
                        rs.getString("product_code"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material"),
                        rs.getString("unit"),
                        rs.getBigDecimal("cost_price"),
                        rs.getBigDecimal("sale_price"),
                        rs.getInt("supplier_id")
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addProduct(Products product) {
        String sql = "INSERT INTO products (product_code, name, description, size, color, material, unit, cost_price, sale_price, supplier_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, product.getProductCode());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getSize());
            ps.setString(5, product.getColor());
            ps.setString(6, product.getMaterial());
            ps.setString(7, product.getUnit());
            ps.setBigDecimal(8, product.getCostPrice());
            ps.setBigDecimal(9, product.getSalePrice());
            ps.setInt(10, product.getSupplierId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Nếu chèn thành công thì trả về true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Delete a product by ID
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Products product) {
        String sql = "UPDATE products SET product_code = ?, name = ?, description = ?, size = ?, color = ?, material = ?, unit = ?, cost_price = ?, sale_price = ?, supplier_id = ? " +
                "WHERE product_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, product.getProductCode());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getSize());
            ps.setString(5, product.getColor());
            ps.setString(6, product.getMaterial());
            ps.setString(7, product.getUnit());
            ps.setBigDecimal(8, product.getCostPrice());
            ps.setBigDecimal(9, product.getSalePrice());
            ps.setInt(10, product.getSupplierId());
            ps.setInt(11, product.getProductId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Get a product by ID
    public Products getProductById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Products(
                        rs.getInt("product_id"),
                        rs.getString("product_code"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material"),
                        rs.getString("unit"),
                        rs.getBigDecimal("cost_price"),
                        rs.getBigDecimal("sale_price"),
                        rs.getInt("supplier_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all products by supplier ID
    public List<Products> getProductsBySupplierId(int supplierId) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE supplier_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, supplierId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Products product = new Products(
                        rs.getInt("product_id"),
                        rs.getString("product_code"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material"),
                        rs.getString("unit"),
                        rs.getBigDecimal("cost_price"),
                        rs.getBigDecimal("sale_price"),
                        rs.getInt("supplier_id")
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get total number of products
    public int getTotalProductCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM products"; // Đếm tổng sản phẩm
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1); // Lấy tổng số lượng
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Get products by page with startIndex and pageSize
    public List<Products> getProductsByPage(int startIndex, int pageSize) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT * FROM products LIMIT ?, ?"; // Query MySQL với LIMIT
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, startIndex);
            ps.setInt(2, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Products product = new Products(
                        rs.getInt("product_id"),
                        rs.getString("product_code"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material"),
                        rs.getString("unit"),
                        rs.getBigDecimal("cost_price"),
                        rs.getBigDecimal("sale_price"),
                        rs.getInt("supplier_id")
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Products> filterProducts(String productCode, String material, BigDecimal costPrice, BigDecimal salePrice, Integer supplierId) {
        List<Products> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");

        if (productCode != null && !productCode.isEmpty()) {
            sql.append(" AND product_code LIKE ?");
        }
        if (material != null && !material.isEmpty()) {
            sql.append(" AND material LIKE ?");
        }
        if (costPrice != null) {
            sql.append(" AND cost_price >= ?");
        }
        if (salePrice != null) {
            sql.append(" AND sale_price <= ?");
        }
        if (supplierId != null) {
            sql.append(" AND supplier_id = ?");
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            int index = 1;

            if (productCode != null && !productCode.isEmpty()) {
                ps.setString(index++, "%" + productCode + "%");
            }
            if (material != null && !material.isEmpty()) {
                ps.setString(index++, "%" + material + "%");
            }
            if (costPrice != null) {
                ps.setBigDecimal(index++, costPrice);
            }
            if (salePrice != null) {
                ps.setBigDecimal(index++, salePrice);
            }
            if (supplierId != null) {
                ps.setInt(index++, supplierId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Products product = new Products(
                        rs.getInt("product_id"),
                        rs.getString("product_code"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material"),
                        rs.getString("unit"),
                        rs.getBigDecimal("cost_price"),
                        rs.getBigDecimal("sale_price"),
                        rs.getInt("supplier_id")
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Products> getFilteredProductsByPage(
            String productCode, String material, BigDecimal costPrice,
            BigDecimal salePrice, Integer supplierId, int startIndex, int pageSize) {

        List<Products> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");

        if (productCode != null && !productCode.isEmpty()) {
            sql.append(" AND product_code LIKE ?");
        }
        if (material != null && !material.isEmpty()) {
            sql.append(" AND material LIKE ?");
        }
        if (costPrice != null) {
            sql.append(" AND cost_price >= ?");
        }
        if (salePrice != null) {
            sql.append(" AND sale_price <= ?");
        }
        if (supplierId != null) {
            sql.append(" AND supplier_id = ?");
        }

        sql.append(" LIMIT ?, ?"); // phân trang

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            int index = 1;

            if (productCode != null && !productCode.isEmpty()) {
                ps.setString(index++, "%" + productCode + "%");
            }
            if (material != null && !material.isEmpty()) {
                ps.setString(index++, "%" + material + "%");
            }
            if (costPrice != null) {
                ps.setBigDecimal(index++, costPrice);
            }
            if (salePrice != null) {
                ps.setBigDecimal(index++, salePrice);
            }
            if (supplierId != null) {
                ps.setInt(index++, supplierId);
            }

            ps.setInt(index++, startIndex);
            ps.setInt(index, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Products product = new Products(
                        rs.getInt("product_id"),
                        rs.getString("product_code"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material"),
                        rs.getString("unit"),
                        rs.getBigDecimal("cost_price"),
                        rs.getBigDecimal("sale_price"),
                        rs.getInt("supplier_id")
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public int getFilteredProductsCount(
            String productCode, String material, BigDecimal costPrice,
            BigDecimal salePrice, Integer supplierId) {

        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products WHERE 1=1");

        if (productCode != null && !productCode.isEmpty()) {
            sql.append(" AND product_code LIKE ?");
        }
        if (material != null && !material.isEmpty()) {
            sql.append(" AND material LIKE ?");
        }
        if (costPrice != null) {
            sql.append(" AND cost_price >= ?");
        }
        if (salePrice != null) {
            sql.append(" AND sale_price <= ?");
        }
        if (supplierId != null) {
            sql.append(" AND supplier_id = ?");
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            int index = 1;

            if (productCode != null && !productCode.isEmpty()) {
                ps.setString(index++, "%" + productCode + "%");
            }
            if (material != null && !material.isEmpty()) {
                ps.setString(index++, "%" + material + "%");
            }
            if (costPrice != null) {
                ps.setBigDecimal(index++, costPrice);
            }
            if (salePrice != null) {
                ps.setBigDecimal(index++, salePrice);
            }
            if (supplierId != null) {
                ps.setInt(index++, supplierId);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }


}