package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class StartupTask extends BackgroundTask {

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    protected User currentUser;
    protected AuthToken authToken;

    public StartupTask(Handler messageHandler) {
        super(messageHandler);
    }

//    @Override
//    protected void runTask(ServerFacade serverFacade) {
//        doTask(serverFacade);
//    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, currentUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }

//    protected abstract void doTask(ServerFacade serverFacade);
    protected abstract void runTask(ServerFacade serverFacade);
}
