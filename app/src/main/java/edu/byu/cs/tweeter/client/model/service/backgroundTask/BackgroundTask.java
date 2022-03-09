package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.util.FakeData;

public abstract class BackgroundTask implements Runnable {
    private static final String LOG_TAG = "BackgroundTask";
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";
    private ServerFacade serverFacade;

    /**
     * Message handler that will receive task results.
     */
    private Handler messageHandler;

    public BackgroundTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    ServerFacade getServerFacade() {
        if(serverFacade == null) {
            serverFacade = new ServerFacade();
        }

        return serverFacade;
    }

    @NonNull
    private Bundle createBundle(boolean b) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, b);
        return msgBundle;
    }

    private void sendMessage(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    protected void sendSuccessMessage() {
        Bundle msgBundle = createBundle(true);
        loadMessageBundle(msgBundle);
        sendMessage(msgBundle);
    }

    protected void sendFailedMessage(String message) {
        Bundle msgBundle = createBundle(false);
        msgBundle.putString(MESSAGE_KEY, message);
        sendMessage(msgBundle);
    }

    protected void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = createBundle(false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);
        sendMessage(msgBundle);
    }

    protected FakeData getFakeData() {
        return new FakeData();
    }

    @Override
    public final void run() {
            runTask(getServerFacade());
    }

    protected abstract void runTask(ServerFacade serverFacade);

    protected abstract void loadMessageBundle(Bundle msgBundle);
}
