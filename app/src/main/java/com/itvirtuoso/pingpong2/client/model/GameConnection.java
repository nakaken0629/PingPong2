package com.itvirtuoso.pingpong2.client.model;

import android.os.Handler;
import android.util.Log;

import com.itvirtuoso.pingpong2.common.PacketType;
import com.itvirtuoso.pingpong2.common.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kenji on 15/02/14.
 */
public class GameConnection {
    private static final String TAG = GameConnection.class.getName();

    private final Handler mHandler;
    private final Socket mSocket;
    private GameListener mListener;

    public static GameConnection createInstance() {
        return new GameConnection();
    }

    private GameConnection() {
        mHandler = new Handler();
        mSocket = new Socket();
    }

    public void connect(final String host, final int port, GameListener listener) {
        mListener = listener;

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new GameRunner(mHandler) {
            @Override
            public void execute() throws IOException {
                SocketAddress address = new InetSocketAddress(host, port);
                mSocket.connect(address);
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            receivePacket();
                        } catch (IOException e) {
                            mListener.onReceiveFail(e);
                        }
                    }
                });
                mListener.onConnectionSuccess();
            }

            @Override
            public void onException(Exception e) {
                mListener.onConnectionFail(e);
            }
        });
    }

    private void sendPacket(PacketType type, byte... data) throws IOException {
        synchronized (mSocket) {
            OutputStream stream = mSocket.getOutputStream();
            stream.write(type.toValue());
            for (byte value : data) {
                stream.write(value);
            }
            stream.write(PacketType.TERMINATE.toValue());
        }
    }

    private void receivePacket() throws IOException {
        InputStream stream = mSocket.getInputStream();
        ArrayList<Integer> data = new ArrayList<>();
        while (true) {
            int value = stream.read();
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

    private void doAction(PacketType type, List<Integer> data) {
        Log.d(TAG, "Packet Type = " + type + ", data = " + StringUtils.join(data, ", "));
        switch (type) {
            case ON_WAIT_CHALLENGER:
                doOnWaitChallenger(data);
                break;
            default:
                Log.w(TAG, type + " is an invalid type");
        }
    }

    public void waitChallenger() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new GameRunner(mHandler) {
            @Override
            public void execute() throws IOException {
                sendPacket(PacketType.WAIT_CHALLENGER);
            }

            @Override
            public void onException(Exception e) {
                mListener.onSendFail(e);
            }
        });
    }

    private void doOnWaitChallenger(List<Integer> data) {
        int gameId = data.get(0) * 256 + data.get(1);
        mListener.onWaitChallenger(gameId);
    }
}
