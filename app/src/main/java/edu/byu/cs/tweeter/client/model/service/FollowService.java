package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers.CountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handlers.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SingleItemObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, PagedNotificationObserver<User> getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken, user, pageSize, lastFollowee, new PagedTaskHandler<>(getFollowingObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void getFollowers(AuthToken authToken, User user, int pageSize, User lastFollower, PagedNotificationObserver<User> getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, user, pageSize, lastFollower, new PagedTaskHandler<>(getFollowersObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void unfollow(AuthToken authToken, User user, SimpleNotificationObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken,
                user, new SimpleNotificationHandler(unfollowObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }

    public void updateSelectedUserFollowingAndFollowers(AuthToken authToken, User user, SingleItemObserver<Integer> getFollowersCountObserver, SingleItemObserver<Integer> getFollowingCountObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                user, new CountHandler(getFollowersCountObserver));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                user, new CountHandler(getFollowingCountObserver));
        executor.execute(followingCountTask);
    }

    public void isFollower(AuthToken authToken, User currentUser, User selectedUser, SingleItemObserver<Boolean> isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                currentUser, selectedUser, new IsFollowerHandler(isFollowerObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public void follow(AuthToken authToken, User user, SimpleNotificationObserver followObserver) {
        FollowTask followTask = new FollowTask(authToken,
                user, new SimpleNotificationHandler(followObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }
}
