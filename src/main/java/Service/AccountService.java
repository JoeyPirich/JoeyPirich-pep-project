package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 *
 * It's perfectly normal to have Service methods that only contain a single line that calls a DAO method. An
 * application that follows best practices will often have unnecessary code, but this makes the code more
 * readable and maintainable in the long run!
 */
public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Register a new account
     * @param account
     * @return the given account with its generated ID if registration successful, else null
     */
    public Account registerAccount(Account account) {
        if (!account.getUsername().isEmpty() 
            && account.getPassword().length() >= 4
            && accountDAO.usernameAvailable(account.getUsername())) {
            return accountDAO.insertAccount(account);
        } else {  // fail and return null if conditions not met
            return null;
        }
    }

    public Account verifyLogin(Account account) {
        return accountDAO.verifyLogin(account);
    }
}
