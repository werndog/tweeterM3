package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SingleItemObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class Presenter {
    public interface View {
        void setIntent(User user);
        void displayErrorMessage(String message);
    }

    private View view;

    public Presenter(View view) {
        this.view = view;
    }

    public class GetUserObserver implements SingleItemObserver<User> {

        @Override
        public void handleSuccess(User user) {
            view.setIntent(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(getMessagePrefix() + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage(getMessagePrefix() + " because of an exception: " + exception.getMessage());
        }
    }

    protected abstract String getMessagePrefix();

}
