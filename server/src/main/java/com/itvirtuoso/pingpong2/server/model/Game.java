package com.itvirtuoso.pingpong2.server.model;

/**
 * Created by nakagaki on 2015/02/09.
 */
public class Game {
    private Player mPlayer1;

    private Game(Player player1) {
        mPlayer1 = player1;
    }

    public static Game create(Player player1) {
        if (player1 == null) {
            throw new IllegalArgumentException("player1 needs instance");
        }
        return new Game(player1);
    }

    public Player getPlayer1() {
        return mPlayer1;
    }
}
