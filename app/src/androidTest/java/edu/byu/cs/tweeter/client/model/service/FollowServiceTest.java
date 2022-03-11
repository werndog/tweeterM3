package edu.byu.cs.tweeter.client.model.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SingleItemObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class FollowServiceTest {

    private User currentUser;
    private AuthToken currentAuthToken;

    private FollowService followServiceSpy;
    private FollowServiceObserver getUsersObserver;

    private CountDownLatch countDownLatch;
//    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
//    private final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);


    /**
     * Create a FollowService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @Before
    public void setup() {
        currentUser = new User("FirstName", "LastName", null);
        currentAuthToken = new AuthToken();

        followServiceSpy = Mockito.spy(new FollowService());

        // Setup an observer for the FollowService
        getUsersObserver = new FollowServiceObserver();

        // Prepare the countdown latch
        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    /**
     * A {@link PagedNotificationObserver<User>} implementation that can be used to get the values
     * eventually returned by an asynchronous call on the {@link FollowService}. Counts down
     * on the countDownLatch so tests can wait for the background thread to call a method on the
     * observer.
     */
    private class FollowServiceObserver implements PagedNotificationObserver<User> {

        private boolean success;
        private String message;
        private List<User> users;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void handleSuccess(List<User> users, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.users = users;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.users = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.success = false;
            this.message = null;
            this.users = null;
            this.hasMorePages = false;
            this.exception = exception;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<User> getUsers() {
            return users;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }

    /**
     * Verify that for successful requests, the {@link FollowService#getFollowing}
     * asynchronous method eventually returns the same result as the {@link ServerFacade}.
     */
    @Test
    public void testGetFollowing_validRequest_correctResponse() throws InterruptedException {
        followServiceSpy.getFollowing(currentAuthToken, currentUser, 3, null, getUsersObserver);
        awaitCountDownLatch();

        List<User> expectedFollowees = new FakeData().getFakeUsers().subList(0, 3);
        Assert.assertTrue(getUsersObserver.isSuccess());
        Assert.assertNull(getUsersObserver.getMessage());
        Assert.assertEquals(expectedFollowees, getUsersObserver.getUsers());
        Assert.assertTrue(getUsersObserver.getHasMorePages());
        Assert.assertNull(getUsersObserver.getException());
    }

    /**
     * Verify that for successful requests, the the {@link FollowService#getFollowing}
     * method loads the profile image of each user included in the result.
     */
    @Test
    public void testGetFollowing_validRequest_loadsProfileImages() throws InterruptedException {
        followServiceSpy.getFollowing(currentAuthToken, currentUser, 3, null, getUsersObserver);
        awaitCountDownLatch();

        List<User> followees = getUsersObserver.getUsers();
        Assert.assertTrue(followees.size() > 0);
    }

    /**
     * Verify that for unsuccessful requests, the the {@link FollowService#getFollowing}
     * method returns the same failure response as the server facade.
     */
    @Test
    public void testGetFollowing_invalidRequest_returnsNoFollowees() throws InterruptedException {
        followServiceSpy.getFollowing(null, null, 0, null, getUsersObserver);
        awaitCountDownLatch();

        Assert.assertFalse(getUsersObserver.isSuccess());
        Assert.assertNull(getUsersObserver.getMessage());
        Assert.assertNull(getUsersObserver.getUsers());
        Assert.assertFalse(getUsersObserver.getHasMorePages());
        Assert.assertNotNull(getUsersObserver.getException());
    }


    @Test
    public void testGetFollowers_validRequest_correctResponse() throws InterruptedException {
        followServiceSpy.getFollowers(currentAuthToken, currentUser, 3, null, getUsersObserver);
        awaitCountDownLatch();

        List<User> expectedFollowees = new FakeData().getFakeUsers().subList(0, 3);
        Assert.assertTrue(getUsersObserver.isSuccess());
        Assert.assertNull(getUsersObserver.getMessage());
        Assert.assertEquals(expectedFollowees, getUsersObserver.getUsers());
        Assert.assertTrue(getUsersObserver.getHasMorePages());
        Assert.assertNull(getUsersObserver.getException());
    }
}
