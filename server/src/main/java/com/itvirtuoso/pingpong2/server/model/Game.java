package com.itvirtuoso.pingpong2.server.model;

/**
 * Created by nakagaki on 2015/02/09.
 */
public class Game {
    private Game() {
        /* nop */
    }

    public static Game create(Player player1) {
        if (player1 == null) {
            throw new IllegalArgumentException("player1 needs instance");
        }
        return null;
    }
}
