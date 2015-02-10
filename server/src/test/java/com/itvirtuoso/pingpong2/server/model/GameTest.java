package com.itvirtuoso.pingpong2.server.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by nakagaki on 2015/02/09.
 */
public class GameTest {
    @Test(expected = IllegalArgumentException.class)
    public void 引数にnullを指定してGameオブジェクトを生成できない() throws Exception {
        Game game = Game.create(null);
    }

    @Test
    public void 一人目のプレイヤーを指定してGameオブジェクトを生成する() throws Exception {
        Player player1 = new Player();
        Game game = Game.create(player1);
        assertEquals("指定した一人目のプレイヤーを取得できない", player1, game.getPlayer1());
    }

    @Test
    public void 二人目のプレイヤーを追加する() throws Exception {
        Player player1 = new Player();
        Game game = Game.create(player1);
        Player player2 = new Player();
        game.addPlayer2(player2);
        assertEquals("追加した二人目のプレイヤーを取得できない", player2, game.getPlayer2());
    }
}
