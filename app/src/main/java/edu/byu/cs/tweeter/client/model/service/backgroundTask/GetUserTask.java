package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {
    private static final String LOG_TAG = "GetUserTask";
    public static final String USER_KEY = "user";

    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String alias;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(messageHandler, authToken);
        this.alias = alias;
    }

    private User getUser() {
        return getFakeData().findUserByAlias(alias);
    }

    @Override
    protected void runTask(ServerFacade serverFacade) {
        // TODO empty cause of dummy data
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, getUser());
    }
}
