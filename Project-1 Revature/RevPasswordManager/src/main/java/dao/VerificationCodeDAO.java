package dao;

import util.DBConnection;
import model.VerificationCode;

import java.sql.*;

public class VerificationCodeDAO {

    // Save a new code or update existing code for the user
    public void saveCode(int userId, String code) throws SQLException {
        String sql = "INSERT INTO verification_codes (user_id, code, expires_at, used) " +
                "VALUES (?, ?, ?, FALSE) " +
                "ON DUPLICATE KEY UPDATE code = ?, expires_at = ?, used = FALSE";

        // Code expires in 5 minutes
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + 5 * 60 * 1000);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, code);
            ps.setTimestamp(3, expiresAt);

            ps.setString(4, code);       // for ON DUPLICATE KEY UPDATE
            ps.setTimestamp(5, expiresAt);

            ps.executeUpdate();
        }
    }

    // Get code for validation (only if not expired and not used)
    public VerificationCode getValidCode(int userId, String code) throws SQLException {
        String sql = "SELECT * FROM verification_codes " +
                "WHERE user_id=? AND code=? AND used=0 AND expires_at >= NOW()";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, code);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                VerificationCode vc = new VerificationCode();
                vc.setCodeId(rs.getInt("code_id"));
                vc.setUserId(rs.getInt("user_id"));
                vc.setCode(rs.getString("code"));
                vc.setExpiresAt(rs.getTimestamp("expires_at"));
                vc.setUsed(rs.getBoolean("used"));  // 0 -> false, 1 -> true
                return vc;
            }
        }
        return null;
    }


    // Mark a code as used
    public void markUsed(int codeId) throws SQLException {
        String sql = "UPDATE verification_codes SET used = TRUE WHERE code_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codeId);
            ps.executeUpdate();
        }
    }

    // Delete code(s) for a user
    public void deleteCode(int userId) throws SQLException {
        String sql = "DELETE FROM verification_codes WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    // Cleanup expired or used codes
    public void deleteExpiredCodes() throws SQLException {
        String sql = "DELETE FROM verification_codes WHERE expires_at < NOW() OR used = TRUE";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }
}
