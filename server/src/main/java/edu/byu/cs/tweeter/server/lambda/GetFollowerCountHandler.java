package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.CountRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowerCountHandler implements RequestHandler<CountRequest, CountResponse> {
    @Override
    public CountResponse handleRequest(CountRequest countRequest, Context context) {
        FollowService followService = new FollowService();
        return followService.getFolloweeCount(countRequest);
    }
}
