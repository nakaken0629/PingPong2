package com.itvirtuoso.pingpong2.server.model;

import java.io.IOException;

/**
 * Created by nakagaki on 2015/02/09.
 */
public interface Player {
    void setPlayerType(PlayerType playerType);
    PlayerType getPlayerType();

    boolean isSwing();
    void onReady() throws IOException;
    void onServe() throws IOException;
    void onFirstBound() throws IOException;
    void onSecondBound() throws IOException;
    void onReturn() throws IOException;
    void onPause() throws IOException;
}
