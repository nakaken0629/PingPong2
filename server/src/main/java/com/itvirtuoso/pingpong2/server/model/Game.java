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
        Game game = new Game(player1);
        player1.setGame(game);
        return game;
    }

    public void addPlayer2(Player player2) {
        if (player2 == null) {
            throw new IllegalArgumentException("player2 needs instance");
        }
        player2.setGame(this);
        mPlayer2 = player2;

        mPlayer1.onReady();
        mPlayer2.onReady();
    }

    public Player getPlayer1() {
        return mPlayer1;
    }

    public Player getPlayer2() {
        return mPlayer2;
    }

    public void onServe() {
        mPlayer1.onServe();
        mPlayer2.onServe();
    }
}
