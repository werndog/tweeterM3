package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SingleItemObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {

    public interface View {
        void displayErrorMessage(String message);
        void cancelPostingToast();
        void updateSelectedUserFollowingAndFollowers();
        void updateFollowButton(boolean b);
        void setFollowerCount(int count);
        void setFolloweeCount(int count);
        void cancelLogoutToast();
        void enableFollowButton(boolean b);
        void logoutUser();
        void displayPostMessage(String message);
    }

    private FollowService followService;
    private StatusService statusService;
    private UserService userService;
    private View view;

    public MainPresenter(View view) {
        this.view = view;
        this.followService = new FollowService();
        this.userService = new UserService();
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public void postStatus(String post) throws ParseException {
        view.displayPostMessage("Posting Status...");
        try {
            Status newStatus = new Status(post,  Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
            getStatusService().postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus, new PostStatusObserver());
        } catch (Exception e) {
            throw(e);
        }
    }

    public void unfollow(User user) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), user, new UnfollowObserver());
    }

    public void follow(User user) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), user, new FollowObserver());
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerObserver());
    }

    public void updateSelectedUserFollowingAndFollowers(User user) {
        followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(), user, new GetFollowersCountObserver(), new GetFollowingCountObserver());
    }

    public void logoutUser() {
        userService.logoutUser(new LogoutObserver());
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }


    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public abstract class MainObserver {
        public void handleFailure(String message) {
            view.displayErrorMessage(getMessagePrefix() + ": " + message);
        }

        public void handleException(Exception exception) {
            view.displayErrorMessage(getMessagePrefix() + " because of an exception: " + exception.getMessage());
        }
        public abstract String getMessagePrefix();
    }

    public abstract class SimpleMainObserver extends MainObserver implements SimpleNotificationObserver {
        public abstract void handleSuccess();
    }

    public abstract class SingleMainObserver<T> extends MainObserver implements SingleItemObserver<T> {
        public abstract void handleSuccess(T notification);
    }

    public abstract class FollowMainObserver implements SimpleNotificationObserver {

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(getMessagePrefix() + ": " + message);
            view.enableFollowButton(true);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage(getMessagePrefix() + " because of an exception: " + exception.getMessage());
            view.enableFollowButton(true);
        }

        public abstract void handleSuccess();
        public abstract String getMessagePrefix();
    }

    public class PostStatusObserver extends SimpleMainObserver implements SimpleNotificationObserver  {

        @Override
        public void handleSuccess() {
            view.cancelPostingToast();
        }

        @Override
        public String getMessagePrefix() {
            return "Failed to post status";
        }
    }

    public class UnfollowObserver extends FollowMainObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            view.updateSelectedUserFollowingAndFollowers();
            view.updateFollowButton(true);
            view.enableFollowButton(true);
        }

        @Override
        public String getMessagePrefix() {
            return "Failed to unfollow";
        }
    }

    public class FollowObserver extends FollowMainObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            view.updateSelectedUserFollowingAndFollowers();
            view.updateFollowButton(false);
            view.enableFollowButton(true);
        }

        @Override
        public String getMessagePrefix() {
            return "Failed to follow";
        }
    }

    public class GetFollowersCountObserver extends SingleMainObserver<Integer> implements SingleItemObserver<Integer> {

        @Override
        public void handleSuccess(Integer count) {
            view.setFollowerCount(count);
        }

        @Override
        public String getMessagePrefix() {
            return "Failed to get followers count";
        }
    }

    public class GetFollowingCountObserver extends SingleMainObserver<Integer> implements SingleItemObserver<Integer> {

        @Override
        public void handleSuccess(Integer count) {
            view.setFolloweeCount(count);
        }

        @Override
        public String getMessagePrefix() {
            return "Failed to get following count";
        }
    }

    public class IsFollowerObserver extends SingleMainObserver<Boolean> implements SingleItemObserver<Boolean> {

        @Override
        public void handleSuccess(Boolean isFollower) {
            view.updateFollowButton(!isFollower);
        }

        @Override
        public String getMessagePrefix() {
            return "Failed to determine following relationship";
        }
    }

    public class LogoutObserver extends SimpleMainObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            view.cancelLogoutToast();
            view.logoutUser();
        }

        @Override
        public String getMessagePrefix() {
            return "Failed to logout";
        }
    }

}
