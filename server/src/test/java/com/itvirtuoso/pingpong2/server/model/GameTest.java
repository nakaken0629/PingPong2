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
        Player player = new Player();
        Game game = Game.create(player);
        assertEquals("指定した一人目のプレイヤーを取得できない", player, game.getPlayer1());
    }
}
