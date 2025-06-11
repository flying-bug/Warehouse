package dal;

import model.Products;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductDAO extends DBConnect {
    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    // Get product by product_code
    public Products getProductByCode(String productCode) {
        String sql = "SELECT * FROM products WHERE product_code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching product by code: " + e.getMessage());
        }
        return null;
    }

    public List<Products> getFilteredProductsByPage(String productCode, String material, BigDecimal costPriceFrom, BigDecimal costPriceTo, int startIndex, int pageSize, Integer status) {
        List<Products> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM products");
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" WHERE status = ?");
            params.add(status);
        } else {
            sql.append(" WHERE 1=1"); // Default condition if no status filter
        }

        if (productCode != null && !productCode.isEmpty()) {
            sql.append(" AND product_code LIKE ?");
            params.add("%" + productCode + "%");
        }
        if (material != null && !material.isEmpty()) {
            sql.append(" AND material LIKE ?");
            params.add("%" + material + "%");
        }
        if (costPriceFrom != null) {
            sql.append(" AND cost_price >= ?");
            params.add(costPriceFrom);
        }
        if (costPriceTo != null) {
            sql.append(" AND cost_price <= ?");
            params.add(costPriceTo);
        }
        sql.append(" LIMIT ?, ?");
        params.add(startIndex);
        params.add(pageSize);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching filtered products by page: " + e.getMessage());
        }
        return products;
    }

    public int getFilteredProductsCount(String productCode, String material, BigDecimal costPriceFrom, BigDecimal costPriceTo, Integer status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products");
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" WHERE status = ?");
            params.add(status);
        } else {
            sql.append(" WHERE 1=1"); // Default condition if no status filter
        }

        if (productCode != null && !productCode.isEmpty()) {
            sql.append(" AND product_code LIKE ?");
            params.add("%" + productCode + "%");
        }
        if (material != null && !material.isEmpty()) {
            sql.append(" AND material LIKE ?");
            params.add("%" + material + "%");
        }
        if (costPriceFrom != null) {
            sql.append(" AND cost_price >= ?");
            params.add(costPriceFrom);
        }
        if (costPriceTo != null) {
            sql.append(" AND cost_price <= ?");
            params.add(costPriceTo);
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching filtered product count: " + e.getMessage());
        }
        return 0;
    }

    public boolean updateProductStatus(int productId, int status) {
        String sql = "UPDATE products SET status = ? WHERE product_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, status); // Set new status (0 for inactive, 1 for active)
            ps.setInt(2, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error updating product status: " + e.getMessage());
            return false;
        }
    }

    public List<Products> getAllProducts() {
        List<Products> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching all products: " + e.getMessage());
        }
        return products;
    }

    public boolean addProduct(Products product) {
        String sql = "INSERT INTO products (product_code, name, description, size, color, material, unit, cost_price, sale_price, status, image, min_stock_level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setProductParameters(ps, product);
            ps.setInt(10, product.getStatus());
            ps.setString(11, product.getImage());
            ps.setInt(12, product.getMinStockLevel());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error adding product: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProduct(Products product) {
        String sql = "UPDATE products SET product_code = ?, name = ?, description = ?, size = ?, color = ?, material = ?, unit = ?, cost_price = ?, sale_price = ?, status = ?, image = ?, min_stock_level = ? " +
                "WHERE product_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setProductParameters(ps, product);
            ps.setInt(10, product.getStatus());
            ps.setString(11, product.getImage());
            ps.setInt(12, product.getMinStockLevel());
            ps.setInt(13, product.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error updating product: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error deleting product: " + e.getMessage());
            return false;
        }
    }

    public Products getProductById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching product by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Products> getProductsByPage(int startIndex, int pageSize) {
        List<Products> products = new ArrayList<>();
        String sql = "SELECT * FROM products LIMIT ?, ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, startIndex);
            ps.setInt(2, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching products by page: " + e.getMessage());
        }
        return products;
    }

    public int getTotalProductCount() {
        String sql = "SELECT COUNT(*) FROM products";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching total product count: " + e.getMessage());
        }
        return 0;
    }

    public List<Products> filterProducts(String productCode, String material, BigDecimal minCostPrice, BigDecimal maxSalePrice) {
        List<Products> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (productCode != null && !productCode.isEmpty()) {
            sql.append(" AND product_code LIKE ?");
            params.add("%" + productCode + "%");
        }
        if (material != null && !material.isEmpty()) {
            sql.append(" AND material LIKE ?");
            params.add("%" + material + "%");
        }
        if (minCostPrice != null) {
            sql.append(" AND cost_price >= ?");
            params.add(minCostPrice);
        }
        if (maxSalePrice != null) {
            sql.append(" AND sale_price <= ?");
            params.add(maxSalePrice);
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error filtering products: " + e.getMessage());
        }
        return products;
    }

    public List<Products> getFilteredProductsByPage(String productCode, String material, BigDecimal minCostPrice, BigDecimal maxSalePrice, int startIndex, int pageSize) {
        List<Products> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (productCode != null && !productCode.isEmpty()) {
            sql.append(" AND product_code LIKE ?");
            params.add("%" + productCode + "%");
        }
        if (material != null && !material.isEmpty()) {
            sql.append(" AND material LIKE ?");
            params.add("%" + material + "%");
        }
        if (minCostPrice != null) {
            sql.append(" AND cost_price >= ?");
            params.add(minCostPrice);
        }
        if (maxSalePrice != null) {
            sql.append(" AND sale_price <= ?");
            params.add(maxSalePrice);
        }
        sql.append(" LIMIT ?, ?");
        params.add(startIndex);
        params.add(pageSize);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching filtered products by page: " + e.getMessage());
        }
        return products;
    }

    public int getFilteredProductsCount(String productCode, String material, BigDecimal minCostPrice, BigDecimal maxSalePrice) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (productCode != null && !productCode.isEmpty()) {
            sql.append(" AND product_code LIKE ?");
            params.add("%" + productCode + "%");
        }
        if (material != null && !material.isEmpty()) {
            sql.append(" AND material LIKE ?");
            params.add("%" + material + "%");
        }
        if (minCostPrice != null) {
            sql.append(" AND cost_price >= ?");
            params.add(minCostPrice);
        }
        if (maxSalePrice != null) {
            sql.append(" AND sale_price <= ?");
            params.add(maxSalePrice);
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching filtered product count: " + e.getMessage());
        }
        return 0;
    }

    public List<Products> getProductsBySupplierId(int supplierId) {
        List<Products> products = new ArrayList<>();
        String sql = "SELECT p.* FROM products p JOIN product_suppliers ps ON p.product_id = ps.product_id WHERE ps.supplier_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching products by supplier ID: " + e.getMessage());
        }
        return products;
    }

    public List<ProductWithInventory> getProductsWithInventory(int warehouseId) {
        List<ProductWithInventory> productList = new ArrayList<>();
        String sql = "SELECT p.*, COALESCE(i.quantity, 0) AS inventory_quantity " +
                "FROM products p LEFT JOIN inventory i ON p.product_id = i.product_id AND i.warehouse_id = ? " +
                "WHERE p.status = 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, warehouseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Products product = mapResultSetToProduct(rs);
                    int inventoryQuantity = rs.getInt("inventory_quantity");
                    productList.add(new ProductWithInventory(product, inventoryQuantity));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching products with inventory: " + e.getMessage());
        }
        return productList;
    }

    public static class ProductWithInventory {
        private Products product;
        private int inventoryQuantity;

        public ProductWithInventory(Products product, int inventoryQuantity) {
            this.product = product;
            this.inventoryQuantity = inventoryQuantity;
        }

        public Products getProduct() {
            return product;
        }

        public int getInventoryQuantity() {
            return inventoryQuantity;
        }
    }

    private Products mapResultSetToProduct(ResultSet rs) throws SQLException {
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
                rs.getInt("status"),
                rs.getString("image"),
                rs.getInt("min_stock_level")
        );
    }

    private void setProductParameters(PreparedStatement ps, Products product) throws SQLException {
        ps.setString(1, product.getProductCode());
        ps.setString(2, product.getName());
        ps.setString(3, product.getDescription());
        ps.setString(4, product.getSize());
        ps.setString(5, product.getColor());
        ps.setString(6, product.getMaterial());
        ps.setString(7, product.getUnit());
        ps.setBigDecimal(8, product.getCostPrice());
        ps.setBigDecimal(9, product.getSalePrice());
    }
}