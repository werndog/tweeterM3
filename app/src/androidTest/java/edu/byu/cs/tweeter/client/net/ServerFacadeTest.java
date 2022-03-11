package edu.byu.cs.tweeter.client.net;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.CountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServerFacadeTest {

    private User fakeUser;
    private AuthToken fakeAuthToken;
    private ServerFacade serverFacadeSpy;

    @Before
    public void setup() {
        fakeUser = new User("Paul", "Bunyon", "@Paul_Bunyon_123", "https://s3.amazon.com/paul_bunyon");
        fakeAuthToken = new AuthToken("abc-123-xyz-789", "August 12, 2021 3:01 PM");

        ServerFacade serverFacade = new ServerFacade();
        serverFacadeSpy = Mockito.spy(serverFacade);
    }

    @Test
    public void testRegister_CorrectParamsPassedToServerFacade() {
        RegisterRequest fakeRegisterRequest = new RegisterRequest("Paul", "Bunyon", "@Paul_Bunyon_123", "password", "https://s3.amazon.com/paul_bunyon");
        try {
            RegisterResponse response = serverFacadeSpy.register(fakeRegisterRequest, "/register");
            Assert.assertNotNull(response);
            Assert.assertEquals(response.getUser(), new FakeData().getFirstUser());
            Assert.assertNull(response.getMessage());
        } catch (Exception ignored) { }
    }

    @Test
    public void testGetFollowers_CorrectParamsPassedToServerFacade() {
        FollowersRequest fakeGetFollowersRequest = new FollowersRequest(fakeAuthToken, "@alias1", 10, "@alias2");
        try {
            FollowersResponse response = serverFacadeSpy.getFollowers(fakeGetFollowersRequest, "/getFollowers");
            List<User> expectedFollowers = new FakeData().getFakeUsers().subList(0, 10);
            Assert.assertNotNull(response);
            Assert.assertEquals(expectedFollowers, response.getFollowers());
            Assert.assertNull(response.getMessage());
            Assert.assertTrue(response.getHasMorePages());
        } catch (Exception ignored) {}
    }

    @Test
    public void testGetFollowerCount_CorrectParamsPassedToServerFacade() {
        CountRequest fakeGetFollowerCountRequest = new CountRequest(fakeAuthToken, fakeUser);
        try {
            CountResponse response = serverFacadeSpy.getCount(fakeGetFollowerCountRequest, "/getFollowerCount");
            Assert.assertNotNull(response);
            Assert.assertEquals(21, response.getCount());
            Assert.assertNull(response.getMessage());
        } catch (Exception ignored) {}
    }
}
