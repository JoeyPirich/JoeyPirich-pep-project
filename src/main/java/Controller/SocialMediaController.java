package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Controller for social media blog API
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("register", this::registerHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::postMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageWithIdHandler);
        app.patch("messages/{message_id}", this::patchMessageWithIdHandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handler for account registration.
     * Register the account represented in the request body.
     * The response will have the added account in the response body and status
     * code 200 if successful, or status code 400 otherwise.
     * 
     * @param ctx contains JSON Account in its body
     * @throws JsonProcessingException
     */
    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.registerAccount(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }
    }

    /**
     * Handler for verifying login credentials.
     * Response will contain the corresponding account in its body with status
     * code 200 if login successful, otherwise gives status code 401.
     * 
     * @param ctx contains JSON Account in its body
     * @throws JsonProcessingException
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account foundAccount = accountService.verifyLogin(account);
        if (foundAccount != null) {
            ctx.json(mapper.writeValueAsString(foundAccount));
        } else {
            ctx.status(401);
        }
    }

    /**
     * Handler for posting a new message.
     * Response body contains the new message in its body with status code 200
     * if successful, otherwise gives status code 400.
     * 
     * @param ctx contains JSON message in its body
     * @throws JsonProcessingException
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message postedMessage = messageService.createMessage(message);
        if (postedMessage != null) {
            ctx.json(mapper.writeValueAsString(postedMessage));
        } else {
            ctx.status(400);
        }
    }

    /**
     * Handler for retrieving all messages.
     * Response body contains a list of all messages with a status code of 200.
     * 
     * @param ctx
     * @throws JsonProcessingException
     */
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        if (messages != null) {
            ctx.json((new ObjectMapper()).writeValueAsString(messages));
        }
    }

    /**
     * Handler for retrieving a message by its ID.
     * Response body contains a the message if found with status code 200.
     * 
     * @param ctx contains "message_id" path param
     * @throws JsonProcessingException
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        Message message = messageService.getMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if (message != null) {
            ObjectMapper mapper = new ObjectMapper();
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    /**
     * Handler for deleting a message with the given ID.
     * Response body contains the deleted message if deletion successful, with
     * status code 200.
     * 
     * @param ctx contains "message_id" path param
     * @throws JsonProcessingException
     */
    private void deleteMessageWithIdHandler(Context ctx) throws JsonProcessingException {
        Message message = messageService.deleteMessageWithId(Integer.parseInt(ctx.pathParam("message_id")));
        if (message != null) {
            ObjectMapper mapper = new ObjectMapper();
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    /**
     * Handler to edit the message text of an existing message with the given ID.
     * Response body contains the edited message with status code 200 if
     * successful, otherwise gives status code 400.
     * 
     * @param ctx contains "message_id" path param and new message test JSON in body
     * @throws JsonProcessingException
     */
    private void patchMessageWithIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = messageService.editMessageWithId(
            Integer.parseInt(ctx.pathParam("message_id")),
            mapper.readValue(ctx.body(), Message.class).getMessage_text());
        if (message != null) {
            ctx.json(mapper.writeValueAsString(message));
        } else {
            ctx.status(400);
        }
    }

    /**
     * Handler to retrieve all messages posted by a given user.
     * Response body contains the list of all messages posted by the user with
     * status code 200.
     * 
     * @param ctx contains "account_id" path param
     * @throws JsonProcessingException
     */
    private void getAllMessagesByUserHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessagesByUser(Integer.parseInt(ctx.pathParam("account_id")));
        if (messages != null) {
            ctx.json((new ObjectMapper()).writeValueAsString(messages));
        }
    }
}