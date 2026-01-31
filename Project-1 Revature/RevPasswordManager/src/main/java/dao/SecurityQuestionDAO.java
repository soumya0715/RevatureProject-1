package dao;

import util.DBConnection;
import model.SecurityQuestion;
import java.sql.*;

public class SecurityQuestionDAO {

    public void addSecurityQuestion(int userId, String question, String answerHash) throws SQLException {
        String sql = "INSERT INTO security_questions (user_id, question, answer_hash) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, question);
            ps.setString(3, answerHash);
            ps.executeUpdate();
        }
    }

    public SecurityQuestion getSecurityQuestion(int userId) throws SQLException {
        String sql = "SELECT * FROM security_questions WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                SecurityQuestion sq = new SecurityQuestion();
                sq.setQuestionId(rs.getInt("question_id"));
                sq.setUserId(rs.getInt("user_id"));
                sq.setQuestion(rs.getString("question"));
                sq.setAnswerHash(rs.getString("answer_hash"));
                return sq;
            }
        }
        return null;
    }

    public void updateSecurityQuestion(int userId, String newQuestion, String newAnswerHash) throws SQLException {
        String sql = "UPDATE security_questions SET question=?, answer_hash=? WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newQuestion);
            ps.setString(2, newAnswerHash);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
    }

    public void deleteSecurityQuestion(int userId) throws SQLException {
        String sql = "DELETE FROM security_questions WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public SecurityQuestion getQuestionByUserId(int userId) throws SQLException {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM security_questions WHERE user_id=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new SecurityQuestion(
                    rs.getInt("question_id"),
                    rs.getInt("user_id"),
                    rs.getString("question"),
                    rs.getString("answer_hash")
            );
        }
        return null;
    }

}