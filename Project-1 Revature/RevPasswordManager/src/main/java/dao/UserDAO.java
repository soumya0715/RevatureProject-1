package dao;
import model.User;
import util.DBConnection;
import java.sql.*;

public class UserDAO {
    public int createUser(String name, String email, String masterPasswordHash) throws SQLException {
        String sql = "INSERT INTO users (name, email, master_password_hash) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, masterPasswordHash);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // Get generated user_id
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the actual user_id
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }


//    public boolean login(String email, String masterPasswordHash) throws SQLException {
//        String sql = "SELECT * FROM users WHERE email=? AND master_password_hash=?";
//        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, email);
//            ps.setString(2, masterPasswordHash);
//            ResultSet rs = ps.executeQuery();
//            return rs.next();
//        }
//    }

    public int getUserIdByEmail(String email) throws SQLException {
        String sql = "SELECT user_id FROM users WHERE email=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("user_id");
        }
        return -1;
    }

    public boolean verifyMasterPassword(int userId, String hash) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id=? AND master_password_hash=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, hash);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void updateProfile(int userId, String newName, String newEmail) throws SQLException {
        String sql = "UPDATE users SET name=?, email=? WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setString(2, newEmail);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
    }

    public void updateMasterPassword(int userId, String newHash) throws SQLException {
        String sql = "UPDATE users SET master_password_hash=? WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
    public String getEmailByUserId(int userId) throws SQLException {
        String sql = "SELECT email FROM users WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        }
        return null;
    }
    public boolean login(String email, String encryptedPassword) throws Exception {
        String sql = """
        SELECT 1 FROM users
        WHERE email = ? AND master_password_hash = ?
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, encryptedPassword);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        }


    }
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT user_id, name, email, master_password_hash FROM users WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setMasterPasswordHash(rs.getString("master_password_hash"));
                return u;
            }
        }
        return null;
    }




}