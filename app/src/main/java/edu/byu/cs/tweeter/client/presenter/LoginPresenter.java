package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class LoginPresenter extends Presenter {

    private UserService userService;

    public LoginPresenter(View view) {
        super(view);
        userService = new UserService();
    }

    public void logIn(String alias, String password) {
        try {
            validateLogin(alias, password);
            userService.logIn(alias, password, new GetUserObserver());
        } catch (Exception e) {
            throw e;
        }
    }

    public void validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    @Override
    protected String getMessagePrefix() {
        return "Failed to login";
    }
}
