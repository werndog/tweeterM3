package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status>{

    private StatusService statusService;

    public StoryPresenter(View view) {
        super(view);
        this.statusService = new StatusService();
    }

    @Override
    protected void getItems(User user, Status lastItem) {
        statusService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetItemsObserver());
    }

    @Override
    protected String getMessagePrefix() {
        return "Failed to get story";
    }
}
