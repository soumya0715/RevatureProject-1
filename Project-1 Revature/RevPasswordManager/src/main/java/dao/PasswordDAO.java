package dao;

import util.DBConnection;
import model.Password;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordDAO {

    public void addPassword(int userId, String accountName, String username, String encryptedPassword) throws SQLException {
        String sql = "INSERT INTO passwords (user_id, account_name, username, encrypted_password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, accountName);
            ps.setString(3, username);
            ps.setString(4, encryptedPassword);
            ps.executeUpdate();
        }
    }

    public List<Password> getPasswords(int userId) throws SQLException {
        List<Password> list = new ArrayList<>();
        String sql = "SELECT * FROM passwords WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Password p = new Password();
                p.setPasswordId(rs.getInt("password_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setAccountName(rs.getString("account_name"));
                p.setUsername(rs.getString("username"));
                p.setEncryptedPassword(rs.getString("encrypted_password"));
                list.add(p);
            }
        }
        return list;
    }

    public Password getPassword(int passwordId) throws SQLException {
        String sql = "SELECT * FROM passwords WHERE password_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, passwordId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Password p = new Password();
                p.setPasswordId(rs.getInt("password_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setAccountName(rs.getString("account_name"));
                p.setUsername(rs.getString("username"));
                p.setEncryptedPassword(rs.getString("encrypted_password"));
                return p;
            }
        }
        return null;
    }

    public void updatePassword(int passwordId, String newEncryptedPassword) throws SQLException {
        String sql = "UPDATE passwords SET encrypted_password=? WHERE password_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newEncryptedPassword);
            ps.setInt(2, passwordId);
            ps.executeUpdate();
        }
    }

    public void deletePassword(int passwordId) throws SQLException {
        String sql = "DELETE FROM passwords WHERE password_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, passwordId);
            ps.executeUpdate();
        }
    }
}