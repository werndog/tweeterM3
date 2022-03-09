package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusDAO {

    public FeedResponse getFeed(FeedRequest request) {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Status> feed = getDummyFeed();
        List<Status> responseFeed = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (feed != null) {
                int feedIndex = getStatusStartingIndex(request.getLastStatus(), feed);

                for(int limitCounter = 0; feedIndex < feed.size() && limitCounter < request.getLimit(); feedIndex++, limitCounter++) {
                    responseFeed.add(feed.get(feedIndex));
                }

                hasMorePages = feedIndex < feed.size();
            }
        }

        return new FeedResponse(responseFeed, hasMorePages);
    }

    public StoryResponse getStory(StoryRequest request) {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Status> story = getDummyFeed();
        List<Status> responseStory = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (story != null) {
                int storyIndex = getStatusStartingIndex(request.getLastStatus(), story);

                for(int limitCounter = 0; storyIndex < story.size() && limitCounter < request.getLimit(); storyIndex++, limitCounter++) {
                    responseStory.add(story.get(storyIndex));
                }

                hasMorePages = storyIndex < story.size();
            }
        }

        return new StoryResponse(responseStory, hasMorePages);
    }

    private int getStatusStartingIndex(Status lastStatus, List<Status> feed) {

        int feedIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < feed.size(); i++) {
                if(lastStatus.equals(feed.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    feedIndex = i + 1;
                    break;
                }
            }
        }

        return feedIndex;
    }

    List<Status> getDummyFeed() {
        return getFakeData().getFakeStatuses();
    }

    FakeData getFakeData() {
        return new FakeData();
    }

}
