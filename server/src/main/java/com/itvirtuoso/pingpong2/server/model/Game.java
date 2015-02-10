package com.itvirtuoso.pingpong2.server.model;

/**
 * Created by nakagaki on 2015/02/09.
 */
public class Game {
    private Player mPlayer1;
    private Player mPlayer2;

    private Game(Player player1) {
        mPlayer1 = player1;
    }

    public static Game create(Player player1) {
        if (player1 == null) {
            throw new IllegalArgumentException("player1 needs instance");
        }
        return new Game(player1);
    }

    public void addPlayer2(Player player2) {
        mPlayer2 = player2;
    }

    public Player getPlayer1() {
        return mPlayer1;
    }

    public Player getPlayer2() {
        return mPlayer2;
    }
}
