package edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;

public class PagedTaskHandler<T> extends BackgroundTaskHandler<PagedNotificationObserver<T>> {

    public PagedTaskHandler(PagedNotificationObserver<T> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedNotificationObserver<T> observer) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }
}
