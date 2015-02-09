package com.itvirtuoso.pingpong2.server.model;

import org.junit.Test;

/**
 * Created by nakagaki on 2015/02/09.
 */
public class GameTest {
    @Test(expected = IllegalArgumentException.class)
    public void 引数にnullを指定してGameオブジェクトを生成できない() throws Exception {
        Game game = Game.create(null);
    }
}
