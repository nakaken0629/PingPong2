package com.itvirtuoso.pingpong2.server.model;

import java.io.IOException;

/**
 * Created by nakagaki on 2015/02/09.
 */
public interface Player {
    void setType(PlayerType playerType);
    PlayerType getType();
    void onReady() throws IOException;
    void onServe() throws IOException;
}
