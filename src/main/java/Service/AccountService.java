package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO = new AccountDAO();

    public Account registerAccount(Account account) {
        // Ensure username is not blank, password length is 4 or more, and account does not exist
        if (account.getUsername() != null && account.getUsername().length() > 0 && account.getPassword().length() >= 4) {
            if (!accountDAO.doesUsernameExist(account.getUsername())) {
                return accountDAO.createAccount(account);
            }
        }
        return null;
    }

    public boolean doesAccountExist(int accountId) {
        Account account = accountDAO.getAccountById(accountId);
        return account != null;
    }

    public boolean doesAccountExistByUsername(String username) {
        Account account = accountDAO.getAccountByUsername(username);  // Assuming you have this method in your DAO
        return account != null;  // Return true if account exists with that username
    }

    public Account validateLogin(String username, String password) {
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }
}
