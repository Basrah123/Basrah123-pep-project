package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO = new MessageDAO();

    public Message createMessage(Message message) {
        // Ensure message_text is valid and posted_by refers to a real user
        if (message.getMessage_text() != null && message.getMessage_text().length() <= 255 && message.getPosted_by() > 0) {
            return messageDAO.createMessage(message);
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public boolean deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public boolean updateMessage(int messageId, String newText) {
        return messageDAO.updateMessageText(messageId, newText);
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }
}
