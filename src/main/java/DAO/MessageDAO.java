package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {
    /**
     * Insert an account into the account table
     * 
     * @param account
     * @return the given account with generated account ID if successful, else null
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generatedMessageId = pkeyResultSet.getInt(1);
                return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Provides a list of all messages in the database
     * 
     * @return list of messages, or null in case of failure
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message";
        List<Message> messages = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                messages.add(new Message(resultSet.getInt("message_id"),
                                         resultSet.getInt("posted_by"),
                                         resultSet.getString("message_text"),
                                         resultSet.getLong("time_posted_epoch")));
            }
            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Provides the message with the given ID if it is in the database,
     * or null otherwise
     * @param message_id
     * @return message with ID if present, else null
     */
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Message(resultSet.getInt("message_id"),
                                   resultSet.getInt("posted_by"),
                                   resultSet.getString("message_text"),
                                   resultSet.getLong("time_posted_epoch"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Delete the message with the given ID. Returns true if the operation
     * executes without error, or false otherwise.
     * 
     * @param message_id
     * @return true if no executed successfully, false in case of exception
     */
    public boolean deleteMessageWithId(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
