package com.itvirtuoso.pingpong2.client.model;

/**
 * Created by kenji on 15/02/14.
 */
public interface GameListener {
    void onConnectionSuccess();
    void onConnectionFail(Exception e);
    void onSendFail(Exception e);
    void onReceiveFail(Exception e);
    void onWaitChallenger(int gameId);
    void onChallengeSuccess();
    void onChallengeFail();
}
