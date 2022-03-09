package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
    protected static final int PAGE_SIZE = 10;
    private T lastItem;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private View view;
    private UserService userService;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }


    public interface View<T> extends Presenter.View {
        void setLoadingFooter(boolean b);
        void addItems(List<T> items);
    }

    public PagedPresenter(View view) {
        super((Presenter.View) view);
        userService = new UserService();
        this.view = view;
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            getItems(user, lastItem);
        }
    }

    public class GetItemsObserver implements PagedNotificationObserver<T> {

        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage(getMessagePrefix() + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage(getMessagePrefix() + " because of exception: " + exception.getMessage());
        }
    }

    public void getUser(String alias) {
        userService.getUser(alias, new GetUserObserver());
    }

    protected abstract void getItems(User user, T lastItem);
}
