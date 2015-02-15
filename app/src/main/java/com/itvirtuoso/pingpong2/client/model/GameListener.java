package com.itvirtuoso.pingpong2.client.model;

/**
 * Created by kenji on 15/02/14.
 */
public interface GameListener {
    public void onConnectionSuccess();
    public void onConnectionFail(Exception e);
    public void onSendFail(Exception e);
    public void onReceiveFail(Exception e);
    public void onWaitChallenger(int gameId);
}
