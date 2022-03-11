package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class CountTask extends AuthenticatedTask {

    public static final String COUNT_KEY = "count";
    protected int count;
    /**
     * The user whose follower count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;

    public CountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
    }
}
