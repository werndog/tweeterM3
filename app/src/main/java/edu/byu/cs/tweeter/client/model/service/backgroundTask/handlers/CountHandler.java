package edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SingleItemObserver;

public class CountHandler extends BackgroundTaskHandler<SingleItemObserver<Integer>> {

    public CountHandler(SingleItemObserver<Integer> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, SingleItemObserver<Integer> observer) {
        int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}
