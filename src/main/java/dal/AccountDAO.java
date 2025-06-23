package dal;

import model.Accounts;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountDAO extends DBConnect {

    public boolean checkEmailExists(String email) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE email = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, email);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
    public int getIdByEmail(String email) {
        String sql = "SELECT AccountId FROM Accounts WHERE Email = ?";
        int userId = -1;
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("account_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }


    public List<Accounts> getAllAccounts() {
        List<Accounts> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Accounts acc = new Accounts(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("role_id"),
                        rs.getString("profile_image"),
                        rs.getInt("status"),
                        rs.getTimestamp("created_at")
                );
                list.add(acc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addAccount(Accounts acc) {
        String sql = "INSERT INTO accounts (username, password, full_name, email, phone, role_id, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, acc.getAccount_name());
            ps.setString(2, acc.getPassword());
            ps.setString(3, acc.getFull_name());
            ps.setString(4, acc.getEmail());
            ps.setString(5, acc.getPhone());
            ps.setInt(6, acc.getRole_id());
            ps.setString(7, acc.getProfile_image());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAccount(int id) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAccount(Accounts acc) {
        String sql = "UPDATE accounts SET username = ?, password = ?, full_name = ?, email = ?, phone = ?, role_id = ?, profile_image = ?, status = ? WHERE account_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, acc.getAccount_name());
            ps.setString(2, acc.getPassword());
            ps.setString(3, acc.getFull_name());
            ps.setString(4, acc.getEmail());
            ps.setString(5, acc.getPhone());
            ps.setInt(6, acc.getRole_id());
            ps.setString(7, acc.getProfile_image());
            ps.setInt(8, acc.getStatus());
            ps.setInt(9, acc.getAccount_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deactivateAccount(int id) {
        String sql = "UPDATE accounts SET status = 0 WHERE account_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public Accounts getAccountByEmail(String email) {
        String sql = "SELECT * FROM accounts WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Accounts(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("role_id"),
                        rs.getString("profile_image"),
                        rs.getInt("status"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Accounts getAccountById(int id) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Accounts(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("role_id"),
                        rs.getString("profile_image"),
                        rs.getInt("status"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Accounts loginAccount(String email, String password) {
        String sql = "SELECT * FROM accounts WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return new Accounts(
                            rs.getInt("account_id"),
                            rs.getString("username"),
                            hashedPassword,
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getInt("role_id"),
                            rs.getString("profile_image"),
                            rs.getInt("status"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isEmailDuplicate(String email) {
        String sql = "SELECT 1 FROM accounts WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPhoneDuplicate(String phone) {
        String sql = "SELECT 1 FROM accounts WHERE phone = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        AccountDAO dao = new AccountDAO();
        Accounts acc = new Accounts();
        acc.setAccount_name("Quy");
        acc.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        acc.setFull_name("Quy");
        acc.setEmail("quyhslc11@gmail.com");
        acc.setPhone("0123456789");
        acc.setRole_id(2);
        acc.setProfile_image("default.jpg");

        boolean success = dao.addAccount(acc);
        System.out.println(success ? "Tạo tài khoản thành công!" : "Tạo tài khoản thất bại.");
    }

    public Map<Integer, Accounts> getAllAccountsMap() {
        Map<Integer, Accounts> map = new HashMap<>();
        String sql = "SELECT * FROM accounts";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Accounts acc = new Accounts(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("role_id"),
                        rs.getString("profile_image"),
                        rs.getInt("status"),
                        rs.getTimestamp("created_at")
                );
                map.put(acc.getAccount_id(), acc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

}
