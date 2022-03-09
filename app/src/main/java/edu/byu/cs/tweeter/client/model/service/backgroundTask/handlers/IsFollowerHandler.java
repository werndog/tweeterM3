package edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SingleItemObserver;

public class IsFollowerHandler extends BackgroundTaskHandler<SingleItemObserver<Boolean>> {

    public IsFollowerHandler(SingleItemObserver<Boolean> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, SingleItemObserver<Boolean> observer) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }
}
