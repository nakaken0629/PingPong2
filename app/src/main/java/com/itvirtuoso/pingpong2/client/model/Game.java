package com.itvirtuoso.pingpong2.client.model;

import android.os.Handler;

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
public class Game {
    private final Handler mHandler;
    private final GameListener mListener;
    private final Socket mSocket;

    public static Game createInstance(Handler handler, GameListener listener) {
        return new Game(handler, listener);
    }

    private Game(Handler handler, GameListener listener) {
        mHandler = handler;
        mSocket = new Socket();
        mListener = listener;
    }

    public void connect(final String host, final int port) {
        if (mSocket.isConnected()) {
            throw new RuntimeException("Socket is already connected.");
        }

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                SocketAddress address = new InetSocketAddress(host, port);
                try {
                    mSocket.connect(address);
                    sendWaitPlayer();
                } catch (final IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onConnectionFail(e);
                        }
                    });
                }
            }
        });
        service.execute(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        int data = mSocket.getInputStream().read();
                        if (data < 0) {
                            mSocket.close();
                        }
                    } catch (IOException e) {
                        /* TODO */
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendWaitPlayer() throws IOException {
        sendPacket(Packet.CONNECT_AS_DEFENDER.getId());
    }

    private void sendPacket(int... packets) throws IOException {
        OutputStream stream = mSocket.getOutputStream();
        for(int packet : packets) {
            stream.write(packet);
        }
        stream.write(Packet.TERMINATE.getId());
    }
}
