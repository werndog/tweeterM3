package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler implements RequestHandler<FollowersRequest, FollowersResponse> {

    @Override
    public FollowersResponse handleRequest(FollowersRequest request, Context context) {
        FollowService service = new FollowService();
        return service.getFollowers(request);
    }
}
