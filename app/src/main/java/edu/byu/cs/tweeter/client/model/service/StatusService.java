package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public void getStory(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedNotificationObserver<Status> getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(authToken,
                user, pageSize, lastStatus, new PagedTaskHandler<>(getStoryObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }

    public void getFeed(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedNotificationObserver<Status> getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken,
                user, pageSize, lastStatus, new PagedTaskHandler<>(getFeedObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    public void postStatus(AuthToken authToken, Status newStatus, SimpleNotificationObserver postStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(authToken,
                newStatus, new SimpleNotificationHandler(postStatusObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(statusTask);
    }
}
