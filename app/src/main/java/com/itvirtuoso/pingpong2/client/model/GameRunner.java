package com.itvirtuoso.pingpong2.client.model;

import android.os.Handler;

import java.io.IOException;

/**
 * Created by kenji on 15/02/15.
 */
public abstract class GameRunner implements Runnable {
    private Handler mHandler;

    public GameRunner(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (final IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onException(e);
                }
            });
        }
    }

    public abstract void execute() throws IOException;
    public abstract void onException(Exception e);
}
