package com.itvirtuoso.pingpong2.server.model;

import com.itvirtuoso.pingpong2.common.GameUtil;
import com.itvirtuoso.pingpong2.common.PacketType;
import com.itvirtuoso.pingpong2.common.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by kenji on 15/02/15.
 */
public class SocketPlayer implements Player, Runnable {
    private static final Logger sLogger = Logger.getLogger(SocketPlayer.class.getName());
    private static final int SWING_TIME = 1000;
    private static final int WAIT_TIME = 1000;

    private Socket mSocket;
    private Game mGame;
    private PlayerType mPlayerType;
    private long mLastSwing = 0;

    public SocketPlayer(Socket socket) {
        mSocket = socket;
    }

    @Override
    public void run() {
        try {
            receivePacket();
        } catch (IOException e) {
            sLogger.warning(e.toString());
            try {
                mSocket.close();
            } catch (IOException e1) {
                sLogger.fine(e1.toString());
            }
        }
    }

    private void sendPacket(PacketType type) throws IOException {
        sendPacket(type, new ArrayList<Integer>());
    }

    private void sendPacket(PacketType type, List<Integer> data) throws IOException {
        sLogger.info("SND Packet Type = " + type + ", data = " + StringUtils.join(data, ", "));
        synchronized (mSocket) {
            OutputStream outputStream = mSocket.getOutputStream();
            outputStream.write(type.toValue());
            for (int value : data) {
                outputStream.write(value);
            }
            outputStream.write(PacketType.TERMINATE.toValue());
        }
    }

    private void receivePacket() throws IOException {
        InputStream inputStream = mSocket.getInputStream();
        ArrayList<Integer> data = new ArrayList<>();
        while (true) {
            int value = inputStream.read();
            if (value < 0) {
                mSocket.close();
                return;
            }
            if (value == PacketType.TERMINATE.toValue()) {
                doAction(PacketType.fromValue(data.get(0)), data.subList(1, data.size()));
                data.clear();
                continue;
            }
            data.add(value);
        }
    }

    @Override
    public void setPlayerType(PlayerType playerType) {
        mPlayerType = playerType;
    }

    @Override
    public PlayerType getPlayerType() {
        return mPlayerType;
    }

    private void doAction(PacketType type, List<Integer> data) throws IOException {
        sLogger.info("RCV Packet Type = " + type + ", data = " + StringUtils.join(data, ", "));
        switch (type) {
            case WAIT_CHALLENGER:
                doWaitChallenger(data);
                break;
            case CHALLENGE:
                doChallenge(data);
                break;
            case SWING:
                doSwing(data);
                break;
            default:
                sLogger.warning(type + " is an invalid type");
        }
    }

    private void doWaitChallenger(List<Integer> receiveData) throws IOException {
        List<Integer> sendData = new ArrayList<>();
        mGame = Game.create(this);
        sendData.addAll(GameUtil.fromId(mGame.getId()));
        sendPacket(PacketType.ON_WAIT_CHALLENGER, sendData);
    }

    private void doChallenge(List<Integer> receiveData) throws IOException {
        int gameId = GameUtil.toId(receiveData.subList(0, 2));
        mGame = Game.get(gameId);
        if (mGame == null) {
            sendPacket(PacketType.ON_CHALLENGE_FAIL);
            return;
        }
        mGame.addPlayer1(this);
        sendPacket(PacketType.ON_CHALLENGE_SUCCESS);

        try {
            mGame.ready();
        } catch (InterruptedException e) {
            sendPacket(PacketType.ON_CHALLENGE_FAIL);
        }
    }

    @Override
    public void onReady() throws IOException {
        sendPacket(PacketType.ON_READY);
    }

    private void doSwing(List<Integer> data) throws IOException {
        long now = System.currentTimeMillis();
        if (now - mLastSwing < WAIT_TIME) {
            return;
        }
        mLastSwing = now;
    }

    @Override
    public boolean isSwing() {
        long now = System.currentTimeMillis();
        return now - mLastSwing < SWING_TIME;
    }

    @Override
    public void onServe() throws IOException {
        sendPacket(PacketType.ON_SERVE);
    }

    @Override
    public void onFirstBound() throws IOException {
        sendPacket(PacketType.ON_FIRST_BOUND);
    }

    @Override
    public void onSecondBound() throws IOException {
        sendPacket(PacketType.ON_SECOND_BOUND);
    }

    @Override
    public void onReturn() throws IOException {
        sendPacket(PacketType.ON_RETURN);
    }

    @Override
    public void onPause() throws IOException {
        sendPacket(PacketType.ON_PAUSE);
    }
}
