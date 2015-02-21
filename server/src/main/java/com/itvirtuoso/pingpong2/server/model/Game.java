package com.itvirtuoso.pingpong2.server.model;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by nakagaki on 2015/02/09.
 */
public class Game {
    private static HashMap<Integer, Game> sGames = new HashMap<>();

    private Integer mId;
    private GameMode mMode;
    private Player mPlayer0;
    private Player mPlayer1;

    private Game(Player player0) {
        mPlayer0 = player0;
        mPlayer0.setPlayerType(PlayerType.PLAYER0);
    }

    public static Game create(Player player0) {
        Game game = new Game(player0);
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

    public void addPlayer1(Player player1) {
        if (player1 == null) {
            throw new IllegalArgumentException("player1 needs instance");
        }
        mPlayer1 = player1;
        mPlayer1.setPlayerType(PlayerType.PLAYER1);
    }

    public void ready() throws IOException, InterruptedException {
        mMode = GameMode.PLAYER0_WAIT_SERVE;
        mPlayer0.onReady();
        mPlayer1.onReady();
    }

    public void swing(Player player) throws IOException {
        if (player.getPlayerType() == PlayerType.PLAYER0 && mMode == GameMode.PLAYER0_WAIT_SERVE) {
            serveAsPlayer0();
            return;
        }
    }

    private void serveAsPlayer0() throws IOException {
        mPlayer0.onServe();
        mPlayer1.onServe();
    }
}
