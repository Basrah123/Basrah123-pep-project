package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            return null; 
        }
    
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
    
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
    
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        message.setMessage_id(generatedKeys.getInt(1));
                        return message;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM Message";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), 
                                              rs.getInt("posted_by"), 
                                              rs.getString("message_text"), 
                                              rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message getMessageById(int messageId) {
        String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM Message WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Message(rs.getInt("message_id"), 
                                   rs.getInt("posted_by"), 
                                   rs.getString("message_text"), 
                                   rs.getLong("time_posted_epoch"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteMessage(int messageId) {
        String sql = "DELETE FROM Message WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, messageId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMessageText(int messageId, String newText) {
        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newText);
            stmt.setInt(2, messageId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Message> getMessagesByUser(int accountId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM Message WHERE posted_by = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                                              rs.getInt("posted_by"),
                                              rs.getString("message_text"),
                                              rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
