package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {
    /**
     * Insert an account into the account table
     * @param account
     * @return the given account with generated account ID if successful, else null
     */
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                // TODO: maybe should be `int generatedAccountId = (int) pkeyResultSet.getLong(1);`?
                int generatedAccountId = pkeyResultSet.getInt(1);
                return new Account(generatedAccountId, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Return whether a given username is available and unclaimed for an
     * account to be registered under it. Defaults to false in case of
     * exception.
     * 
     * @param username
     * @return true if an account exists with the given username
     */
    public boolean usernameAvailable(String username) {
        Connection connection = ConnectionUtil.getConnection();
        String checkSql = "SELECT * FROM account WHERE username=?";

        try {
            PreparedStatement checkPreparedStatement = connection.prepareStatement(checkSql);
            checkPreparedStatement.setString(1, username);
            ResultSet checkResultSet = checkPreparedStatement.executeQuery();
            return !checkResultSet.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Check whether a registered account exists with a username and password
     * matching the given account. If so, return the full account including
     * account ID, otherwise, return false.
     * 
     * @param account
     * @return account if found, otherwise null
     */
    public Account verifyLogin(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username=? AND password=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Account(resultSet.getInt("account_id"),
                                   resultSet.getString("username"),
                                   resultSet.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
