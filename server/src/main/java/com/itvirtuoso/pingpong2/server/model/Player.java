package com.itvirtuoso.pingpong2.server.model;

import java.io.IOException;

/**
 * Created by nakagaki on 2015/02/09.
 */
public interface Player {
    void setPlayerType(PlayerType playerType);
    PlayerType getPlayerType();
    void onReady() throws IOException;
    void onServe() throws IOException;
}
