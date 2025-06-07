package dal;

import model.Accounts;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends DBConnect {

    // Lấy danh sách tất cả tài khoản
    public List<Accounts> getAllAccounts() {
        List<Accounts> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Accounts acc = new Accounts(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("full_name"),
                        rs.getString("profile_image"),
                        rs.getInt("role_id")
                );
                list.add(acc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addAccount(Accounts acc) {
        String sql = "INSERT INTO accounts (username, password, email, phone, full_name, profile_image, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, acc.getAccount_name());
            ps.setString(2, acc.getPassword());
            ps.setString(3, acc.getEmail());
            ps.setString(4, acc.getPhone());
            ps.setString(5, acc.getFull_name());
            ps.setString(6, acc.getProfile_image());
            ps.setInt(7, acc.getRole_id());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0; // true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // lỗi -> trả về false
        }
    }


    public boolean deleteAccount(int id) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateAccount(Accounts acc) {
        String sql = "UPDATE accounts SET username = ?, password = ?, email = ?, phone = ?, full_name = ?, profile_image = ?, role_id = ? WHERE account_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, acc.getAccount_name());
            ps.setString(2, acc.getPassword());
            ps.setString(3, acc.getEmail());
            ps.setString(4, acc.getPhone());
            ps.setString(5, acc.getFull_name());
            ps.setString(6, acc.getProfile_image());
            ps.setInt(7, acc.getRole_id());
            ps.setInt(8, acc.getAccount_id());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // true nếu có ít nhất 1 row được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Lấy tài khoản theo ID
    public Accounts getAccountById(int id) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Accounts(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("full_name"),
                        rs.getString("profile_image"),
                        rs.getInt("role_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Accounts loginAccount(String email, String password) {
        String sql = "SELECT * FROM accounts WHERE email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                // Nếu là admin@example.com với mật khẩu hash cố định, vào luôn
                if ("admin1@gmail.com".equals(email)
                        && "$2a$12$CwTycUXWue0Thq9StjUM0uJ8r6pWxv99F3nZq6m97Xhnz8z3PQY1e".equals(hashedPassword)) {
                    return new Accounts(
                            rs.getInt("account_id"),
                            rs.getString("username"),
                            hashedPassword,
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("full_name"),
                            rs.getString("profile_image"),
                            rs.getInt("role_id")
                    );
                }

                // Ngược lại, kiểm tra mật khẩu nhập với hash bình thường
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return new Accounts(
                            rs.getInt("account_id"),
                            rs.getString("username"),
                            hashedPassword,
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("full_name"),
                            rs.getString("profile_image"),
                            rs.getInt("role_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    // Kiểm tra email đã tồn tại hay chưa
    public boolean isEmailDuplicate(String email) {
        String sql = "SELECT 1 FROM accounts WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // nếu có dòng trả về -> email trùng
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra số điện thoại đã tồn tại hay chưa
    public boolean isPhoneDuplicate(String phone) {
        String sql = "SELECT 1 FROM accounts WHERE phone = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // nếu có dòng trả về -> phone trùng
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
