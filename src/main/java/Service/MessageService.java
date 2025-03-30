package Service;

import Model.Message;

import static org.mockito.ArgumentMatchers.anyFloat;

import DAO.AccountDAO;
import DAO.MessageDAO;

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
}
