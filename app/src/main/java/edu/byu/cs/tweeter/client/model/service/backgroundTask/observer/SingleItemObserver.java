package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

public interface SingleItemObserver<T> extends ServiceObserver {
    void handleSuccess(T item);
}
