package Service;

import Model.Message;

import static org.mockito.ArgumentMatchers.anyFloat;

import DAO.AccountDAO;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private AccountDAO accountDAO;
    private MessageDAO messageDAO;

    public MessageService() {
        accountDAO = new AccountDAO();
        messageDAO = new MessageDAO();
    }

    /**
     * Add a message, only if its text is at least one and no more than 255
     * characters, and the user it is posted by actually exists in the
     * database. Return the message if successful, otherwise return null.
     * 
     * @param message
     * @return the given message with its assigned ID if adding was successful,
     * else null.
     */
    public Message createMessage(Message message) {
        if (!message.getMessage_text().isEmpty()
            && message.getMessage_text().length() <= 0xFF
            && accountDAO.getAccountById(message.posted_by) != null) {
            return messageDAO.insertMessage(message);
        } else {
            return null;
        }
    }

    /**
     * Provides a list of all messages in the database
     * 
     * @return list of messages, or null in case of failure
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Provides the message with the given ID if it is in the database,
     * or null otherwise
     * @param messageId
     * @return message with ID if present, else null
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
}
