package com.itvirtuoso.pingpong2.client.model;

/**
 * Created by kenji on 15/02/15.
 */
public enum Packet {
    TERMINATE(0),
    CONNECT_AS_DEFENDER(1),
    ;

    private final int mId;

    private Packet(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }
}
