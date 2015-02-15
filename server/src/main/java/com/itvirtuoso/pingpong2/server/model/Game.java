package com.itvirtuoso.pingpong2.server.model;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by nakagaki on 2015/02/09.
 */
public class Game {
    private static HashMap<Integer, Game> sGames = new HashMap<>();

    private Integer mId;
    private Player mPlayer1;
    private Player mPlayer2;

    private Game(Player player1) {
        mPlayer1 = player1;
    }

    public static Game create(Player player1) {
        Game game = new Game(player1);
        while(true) {
            Integer id = Integer.valueOf((int) (Math.random() * 9000) + 1000);
            if (!sGames.containsKey(id)) {
                game.mId = id;
                sGames.put(id, game);
                break;
            }
        }
        return game;
    }

    public int getId() {
        return Integer.valueOf(mId);
    }

    public static Game get(int id) {
        return sGames.get(Integer.valueOf(id));
    }

    public void addPlayer2(Player player2) {
        if (player2 == null) {
            throw new IllegalArgumentException("player2 needs instance");
        }
        mPlayer2 = player2;
    }

    public void ready() throws IOException, InterruptedException {
        Thread.sleep(1000);
        mPlayer1.onReady();
        mPlayer2.onReady();
    }
}
