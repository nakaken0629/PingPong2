package com.itvirtuoso.pingpong2.common;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kenji on 15/02/15.
 */
public final class GameUtil {
    private static final int CARDINAL = 100;

    private GameUtil() {
        /* nop */
    }

    public static List<Integer> fromId(int id) {
        return Arrays.asList(id / CARDINAL, id % CARDINAL);
    }

    public static int toId(List<Integer> data) {
        return data.get(0) * CARDINAL + data.get(1);
    }
}
