package com.itvirtuoso.pingpong2.server.model;

/**
 * Created by nakagaki on 2015/02/09.
 */
public abstract class Player {
    private Game mGame;

    public abstract void onReady();
    public abstract void onServe();

    public void setGame(Game game) {
        mGame = game;
    }

    public void serve() {
        mGame.onServe();
    }
}
