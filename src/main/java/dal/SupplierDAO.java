package dal;

import model.Suppliers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO extends DBConnect {

    // Get all suppliers
    public List<Suppliers> getAllSuppliers() {
        List<Suppliers> list = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Suppliers supplier = new Suppliers(
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
                list.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Add a new supplier
    public void addSupplier(Suppliers supplier) {
        String sql = "INSERT INTO suppliers (name, phone, address) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getPhone());
            ps.setString(3, supplier.getAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a supplier by ID
    public boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, supplierId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update supplier details and return success status
    public boolean updateSupplier(Suppliers supplier) {
        String sql = "UPDATE suppliers SET name = ?, phone = ?, address = ? WHERE supplier_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getPhone());
            ps.setString(3, supplier.getAddress());
            ps.setInt(4, supplier.getSupplierId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có bản ghi bị ảnh hưởng
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }


    // Get supplier by ID
    public Suppliers getSupplierById(int supplierId) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, supplierId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Suppliers(
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Check if supplier name or phone already exists
    public boolean isDuplicateSupplier(String name, String phone) {
        String sql = "SELECT COUNT(*) FROM suppliers WHERE name = ? OR phone = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isDuplicateSupplierName(String name) {
        String sql = "SELECT 1 FROM suppliers WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();  // Nếu có bản ghi thì trả về true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isDuplicatePhone(String phone) {
        String sql = "SELECT 1 FROM suppliers WHERE phone = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




}