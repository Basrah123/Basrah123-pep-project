package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    private AccountService accountService = new AccountService();
    private MessageService messageService = new MessageService();

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);

        if (accountService.doesAccountExistByUsername(account.getUsername())) {
            ctx.status(400).result("");  
            return;
        }
        Account createdAccount = accountService.registerAccount(account);
        if (createdAccount != null) {
            ctx.status(200).json(createdAccount);  
        } else {
            ctx.status(400).result("");  
        }
    }
    
    

    private void loginHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);

        Account loginAccount = accountService.validateLogin(account.getUsername(), account.getPassword());

        if (loginAccount != null) {
            ctx.status(200).json(loginAccount);
        } else {
            ctx.status(401).result("");  
        }
    }  

    private void createMessageHandler(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
    
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            ctx.status(400).result("");
            return;
        }
    
        if (!accountService.doesAccountExist(message.getPosted_by())) {
            // Return 400 Bad Request with an empty body if the user doesn't exist
            ctx.status(400).result("");
            return;
        }

        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            ctx.status(200).json(createdMessage);
        } else {
            ctx.status(400).result("Failed to create message"); 
        }
    }
    

    private void getAllMessagesHandler(Context ctx) {
        ctx.status(200).json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
    
        if (message != null) {
            ctx.status(200).json(message);
        } else {
            ctx.status(200).result(""); 
        }
    }

    private void deleteMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId); 
    
        if (message != null) {
            boolean isDeleted = messageService.deleteMessage(messageId);
        
            if (isDeleted) {
                ctx.status(200).json(message);
            } else {
                ctx.status(400).result(""); 
            }
            
        } else {
            ctx.status(200).result("");
        }
    }
    
    

    private void updateMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = ctx.bodyAsClass(Message.class);
        String newMessageText = message.getMessage_text().trim();
  
        if (newMessageText.isEmpty() || newMessageText.length() > 255) {
            ctx.status(400).result(""); 
            return;
        }

        Message existingMessage = messageService.getMessageById(messageId);
        if (existingMessage == null) {
            ctx.status(400).result("");
            return;
        }
 
        boolean isUpdated = messageService.updateMessage(messageId, newMessageText);
        if (isUpdated) {
            existingMessage.setMessage_text(newMessageText);
            ctx.status(200).json(existingMessage);
        } else {
            ctx.status(400).result("");
        }
    }    

    private void getMessagesByUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.status(200).json(messageService.getMessagesByUser(accountId));
    }

}