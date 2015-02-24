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

    private int mInterval = 1000;
    private long mNextTime;

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
        mMode = GameMode.PLAYER0_SERVE;
        mService.execute(this);
        mService.shutdown();
        mPlayer0.onReady();
        mPlayer1.onReady();
    }

    private void refreshNextTime(long interval) {
        mNextTime = System.currentTimeMillis() + interval;
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
        while (true) {
            Thread.yield();
            if (System.currentTimeMillis() < mNextTime) {
                continue;
            }
            switch (mMode) {
                case PLAYER0_SERVE:
                    doPlayer0Serve();
                    break;
                case PLAYER0_RETURN:
                    doPlayer0Return();
                    break;
                case PLAYER0_FIRST_BOUND:
                    doPlayer0FirstBound();
                    break;
                case PLAYER0_SECOND_BOUND:
                    doPlayer0SecondBound();
                    break;
                case PLAYER1_SERVE:
                    doPlayer1Serve();
                    break;
                case PLAYER1_RETURN:
                    doPlayer1Return();
                    break;
                case PLAYER1_FIRST_BOUND:
                    doPlayer1FirstBound();
                    break;
                case PLAYER1_SECOND_BOUND:
                    doPlayer1SecondBound();
                    break;
                default:
                    /* nop */
            }
        }
    }

    private void doPlayer0Serve() throws IOException, InterruptedException {
        if (mPlayer0.isSwing()) {
            mPlayer0.onServe();
            mPlayer1.onServe();
            refreshNextTime(mInterval);
            mMode = GameMode.PLAYER0_FIRST_BOUND;
        }
    }

    private void doPlayer0Return() throws IOException, InterruptedException {
        if (mPlayer0.isSwing()) {
            mPlayer0.onReturn();
            mPlayer1.onReturn();
            refreshNextTime(mInterval * 2);
            mMode = GameMode.PLAYER0_SECOND_BOUND;
        } else {
            mPlayer0.onPause();
            mPlayer1.onPause();
            mMode = GameMode.PLAYER1_SERVE;
        }
    }

    private void doPlayer0FirstBound() throws IOException, InterruptedException {
        mPlayer0.onFirstBound();
        mPlayer1.onFirstBound();
        refreshNextTime(mInterval);
        mMode = GameMode.PLAYER0_SECOND_BOUND;
    }

    private void doPlayer0SecondBound() throws IOException, InterruptedException {
        mPlayer0.onSecondBound();
        mPlayer1.onSecondBound();
        refreshNextTime(mInterval);
        mMode = GameMode.PLAYER1_RETURN;
    }

    private void doPlayer1Serve() throws IOException, InterruptedException {
        if (mPlayer1.isSwing()) {
            mPlayer0.onServe();
            mPlayer1.onServe();
            refreshNextTime(mInterval);
            mMode = GameMode.PLAYER1_FIRST_BOUND;
        }
    }

    private void doPlayer1Return() throws IOException, InterruptedException {
        if (mPlayer1.isSwing()) {
            mPlayer0.onReturn();
            mPlayer1.onReturn();
            refreshNextTime(mInterval * 2);
            mMode = GameMode.PLAYER1_SECOND_BOUND;
        } else {
            mPlayer0.onPause();
            mPlayer1.onPause();
            mMode = GameMode.PLAYER0_SERVE;
        }
    }

    private void doPlayer1FirstBound() throws IOException, InterruptedException {
        mPlayer0.onFirstBound();
        mPlayer1.onFirstBound();
        refreshNextTime(mInterval);
        mMode = GameMode.PLAYER1_SECOND_BOUND;
    }

    private void doPlayer1SecondBound() throws IOException, InterruptedException {
        mPlayer0.onSecondBound();
        mPlayer1.onSecondBound();
        refreshNextTime(mInterval);
        mMode = GameMode.PLAYER0_RETURN;
    }
}
