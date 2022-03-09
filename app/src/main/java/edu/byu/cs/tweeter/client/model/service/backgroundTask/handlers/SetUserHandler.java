package edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SingleItemObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class SetUserHandler extends BackgroundTaskHandler<SingleItemObserver<User>> {

    public SetUserHandler(SingleItemObserver<User> observer) { super(observer); }

    @Override
    protected void handleSuccess(Bundle data, SingleItemObserver<User> observer) {
        User currentUser = (User) data.getSerializable(LoginTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);

        // Cache user session information
        Cache.getInstance().setCurrUser(currentUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        observer.handleSuccess(currentUser);
    }
}
