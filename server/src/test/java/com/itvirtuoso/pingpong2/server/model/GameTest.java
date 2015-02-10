package com.itvirtuoso.pingpong2.server.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by nakagaki on 2015/02/09.
 */
public class GameTest {
    class NothingPlayer extends Player {
        @Override
        public void onReady() {
            /* NOP */
        }

        @Override
        public void onServe() {
            /* NOP */
        }
    }
    @Test
    public void 一人目のプレイヤーを指定してGameオブジェクトを生成する() throws Exception {
        Player player1 = new NothingPlayer();
        Game game = Game.create(player1);
        assertEquals("指定した一人目のプレイヤーを取得できない", player1, game.getPlayer1());
    }

    @Test(expected = IllegalArgumentException.class)
    public void 引数にnullを指定してGameオブジェクトを生成できない() throws Exception {
        Game.create(null);
    }

    @Test
    public void 二人目のプレイヤーを追加する() throws Exception {
        Player player1 = new NothingPlayer();
        Game game = Game.create(player1);
        Player player2 = new NothingPlayer();
        game.addPlayer2(player2);
        assertEquals("追加した二人目のプレイヤーを取得できない", player2, game.getPlayer2());
    }

    @Test(expected = IllegalArgumentException.class)
    public void player2にnullを指定できない() throws Exception {
        Game game = Game.create(new NothingPlayer());
        game.addPlayer2(null);
    }

    @Test
    public void 二人目が参加したらゲームが開始できることをPlayerに伝える() throws Exception {
        class ReadyPlayer extends NothingPlayer {
            private boolean mIsCalledOnReady = false;
            @Override
            public void onReady() {
                mIsCalledOnReady = true;
            }
        }
        ReadyPlayer player1 = new ReadyPlayer();
        Game game = Game.create(player1);
        ReadyPlayer player2 = new ReadyPlayer();
        game.addPlayer2(player2);

        assertTrue("player1のonReadyが呼び出されない", player1.mIsCalledOnReady);
        assertTrue("player2のonReadyが呼び出されない", player2.mIsCalledOnReady);
    }

    @Test
    public void サーブをうつ() throws Exception {
        class ServePlayer extends NothingPlayer {
            private boolean mIsCallOnServe = false;

            @Override
            public void onServe() {
                mIsCallOnServe = true;
            }
        }
        ServePlayer player1 = new ServePlayer();
        Game game = Game.create(player1);
        ServePlayer player2 = new ServePlayer();
        game.addPlayer2(player2);

        player1.serve();
        assertTrue("player1にサーブイベントが伝わっていない", player1.mIsCallOnServe);
        assertTrue("player2にサーブイベントが伝わっていない", player2.mIsCallOnServe);
    }
}
