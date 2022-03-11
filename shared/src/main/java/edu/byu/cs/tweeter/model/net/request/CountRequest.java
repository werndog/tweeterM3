package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class CountRequest {

    private AuthToken authToken;
    private User user;

    private CountRequest() {}

    public CountRequest(AuthToken authToken, User user) {
        this.authToken = authToken;
        this.user = user;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUserAlias(User user) {
        this.user = user;
    }
}
