package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User>{

    private FollowService followService;

    public FollowersPresenter(View view) {
        super(view);
        this.followService = new FollowService();
    }


    @Override
    protected void getItems(User user, User lastItem) {
        followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetItemsObserver());
    }

    @Override
    protected String getMessagePrefix() {
        return "Failed to get followers";
    }
}
