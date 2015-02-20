package com.itvirtuoso.pingpong2.common;
/**
 * Created by kenji on 15/02/15.
 */
public enum PacketType {
    WAIT_CHALLENGER(1),
    CHALLENGE(2),
    ON_WAIT_CHALLENGER(101),
    ON_CHALLENGE_FAIL(102),
    ON_CHALLENGE_SUCCESS(103),
    ON_READY(104),
    TERMINATE(255);

    private final int mValue;

    private PacketType(int value) {
        mValue = value;
    }

    public int toValue() {
        return mValue;
    }

    public static PacketType fromValue(int value) {
        for(PacketType packetType : values()) {
            if (packetType.toValue() == value) {
                return packetType;
            }
        }
        throw new IllegalArgumentException();
    }
}