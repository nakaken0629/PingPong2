package com.itvirtuoso.pingpong2.server.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by nakagaki on 2015/02/09.
 */
public class Game implements Runnable {
    private static final Logger sLogger = Logger.getLogger(Game.class.getName());
    private static final Object sLock = new Object();
    private static HashMap<Integer, Game> sGames = new HashMap<>();

    private Integer mId;
    private GameMode mMode;
    private Player mPlayer0;
    private Player mPlayer1;

    private int interval = 500;

    private ExecutorService mService = Executors.newSingleThreadExecutor();

    private Game(Player player0) {
        mPlayer0 = player0;
        mPlayer0.setPlayerType(PlayerType.PLAYER0);
    }

    public static Game create(Player player0) {
        Game game = new Game(player0);
        synchronized (sLock) {
            while (true) {
                Integer id = Integer.valueOf((int) (Math.random() * 9000) + 1000);
                if (!sGames.containsKey(id)) {
                    game.mId = id;
                    sGames.put(id, game);
                    break;
                }
            }
        }
        return game;
    }

    public int getId() {
        return Integer.valueOf(mId);
    }

    public static Game get(int id) {
        synchronized (sLock) {
            return sGames.get(Integer.valueOf(id));
        }
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
        mService.execute(this);
        mService.shutdown();
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
        mMode = GameMode.PLAYER0_SERVE;
    }

    @Override
    public void run() {
        try {
            runInner();
        } catch (IOException | InterruptedException e) {
            sLogger.severe(e.toString());
            /* TODO: ゲーム中断処理 */
        }
    }

    private void runInner() throws IOException, InterruptedException {
        while(true) {
            switch (mMode) {
                case PLAYER0_SERVE:
                    mPlayer0.onServe();
                    mPlayer1.onServe();
                    Thread.sleep(interval);
                    mMode = GameMode.PLAYER0_FIRST_BOUND;
                    break;

                case PLAYER0_FIRST_BOUND:
                    mPlayer0.onFirstBound();
                    mPlayer1.onFirstBound();
                    Thread.sleep(interval);
                    mMode = GameMode.PLAYER0_SECOND_BOUND;
                    break;

                case PLAYER0_SECOND_BOUND:
                    mPlayer0.onSecondBound();
                    mPlayer1.onSecondBound();
                    Thread.sleep(interval);
                    mMode = GameMode.PLAYER0_WAIT_SERVE;
                    break;

                default:
                    Thread.yield();
            }
        }
    }
}
