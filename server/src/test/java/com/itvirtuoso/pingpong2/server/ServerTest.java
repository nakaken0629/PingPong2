package com.itvirtuoso.pingpong2.server;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by nakagaki on 2015/02/09.
 */
public class ServerTest {
    @Test
    public void test失敗サンプル() {
        Server server = new Server();
        fail("失敗テストケース");
    }

    @Test
    public void test成功サンプル() {
        assertTrue("成功テストケース", true);
    }
}
