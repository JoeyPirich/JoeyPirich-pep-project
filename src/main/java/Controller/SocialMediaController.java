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
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
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

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        if (messages != null) {
            ctx.json((new ObjectMapper()).writeValueAsString(messages));
        }
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        Message message = messageService.getMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if (message != null) {
            ObjectMapper mapper = new ObjectMapper();
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    private void deleteMessageWithIdHandler(Context ctx) throws JsonProcessingException {
        Message message = messageService.deleteMessageWithId(Integer.parseInt(ctx.pathParam("message_id")));
        if (message != null) {
            ObjectMapper mapper = new ObjectMapper();
            ctx.json(mapper.writeValueAsString(message));
        }
    }

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

    private void getAllMessagesByUserHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessagesByUser(Integer.parseInt(ctx.pathParam("account_id")));
        if (messages != null) {
            ctx.json((new ObjectMapper()).writeValueAsString(messages));
        }
    }
}