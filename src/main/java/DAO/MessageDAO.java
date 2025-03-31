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
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        
        try {
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

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Message> messages = new ArrayList<>();
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

    /**
     * Patch the message with the given ID to have the given message text.
     * Returns true if the SQL executes successfully, false otherwise.
     * 
     * @param message_id
     * @param message_text
     * @return true in case of successful execution, false in case of exception
     */
    public boolean editMessageWithId(int message_id, String message_text) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text=? WHERE message_id=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message_text);
            preparedStatement.setInt(2, message_id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Returns a list of all messages posted by a user with a given ID
     * @param posted_by
     * @return list of messages, or null in case of exception
     */
    public List<Message> getAllMessagesByUser(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE posted_by=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, posted_by);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Message> messages = new ArrayList<>();
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
}
