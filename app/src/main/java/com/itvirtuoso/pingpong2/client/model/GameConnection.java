package com.itvirtuoso.pingpong2.client.model;

import android.os.Handler;

import com.itvirtuoso.pingpong2.common.PacketType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kenji on 15/02/14.
 */
public class GameConnection {
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

    private void sendPacket(PacketType type, int... data) throws IOException {
        synchronized (mSocket) {
            OutputStream stream = mSocket.getOutputStream();
            stream.write(type.toValue());
            for (int value : data) {
                stream.write(value);
            }
            stream.write(PacketType.TERMINATE.toValue());
        }
    }

    public void connect(final String host, final int port, GameListener listener) {
        mListener = listener;

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new GameRunner(mHandler) {
            @Override
            public void execute() throws IOException {
                SocketAddress address = new InetSocketAddress(host, port);
                mSocket.connect(address);
                mListener.onConnectionSuccess();
            }

            @Override
            public void onException(Exception e) {
                mListener.onConnectionFail(e);
            }
        });
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
}
