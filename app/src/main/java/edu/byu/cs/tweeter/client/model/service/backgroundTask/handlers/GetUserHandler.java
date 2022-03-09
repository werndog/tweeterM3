package edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SingleItemObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserHandler extends BackgroundTaskHandler<SingleItemObserver<User>> {

    public GetUserHandler(SingleItemObserver<User> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, SingleItemObserver<User> observer) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        observer.handleSuccess(user);
    }
}