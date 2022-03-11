package edu.byu.cs.tweeter.client.model.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusServiceTest {

    private User currentUser;
    private AuthToken currentAuthToken;

    private StatusService statusServiceSpy;
    private StatusServiceObserver observer;

    private CountDownLatch countDownLatch;

    @Before
    public void setup() {
        currentUser = new User("FirstName", "LastName", null);
        currentAuthToken = new AuthToken();

        statusServiceSpy = Mockito.spy(new StatusService());

        // Setup an observer for the StatusService
        observer = new StatusServiceObserver();

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

    private class StatusServiceObserver implements PagedNotificationObserver<Status> {
        private boolean success;
        private String message;
        private List<Status> story;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void handleSuccess(List<Status> story, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.story = story;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.story = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.success = false;
            this.message = null;
            this.story = null;
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

        public List<Status> getStory() {
            return story;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws InterruptedException {
        statusServiceSpy.getStory(currentAuthToken, currentUser, 2, null, observer);
        awaitCountDownLatch();

        List<Status> expectedStory = new FakeData().getFakeStatuses().subList(0, 2);
        Assert.assertTrue(observer.isSuccess());
        Assert.assertNull(observer.getMessage());
        List<Status> newStory = observer.getStory();
        Assert.assertEquals(expectedStory.get(0).post, newStory.get(0).post);
        Assert.assertEquals(expectedStory.get(0).mentions, newStory.get(0).mentions);
        Assert.assertEquals(expectedStory.get(0).urls, newStory.get(0).urls);
        Assert.assertEquals(expectedStory.get(1).post, newStory.get(1).post);
        Assert.assertEquals(expectedStory.get(1).mentions, newStory.get(1).mentions);
        Assert.assertEquals(expectedStory.get(1).urls, newStory.get(1).urls);

        Assert.assertTrue(observer.getHasMorePages());
        Assert.assertNull(observer.getException());
    }
}
