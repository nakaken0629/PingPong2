package com.itvirtuoso.pingpong2.server.model;

import com.itvirtuoso.pingpong2.common.PacketType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by kenji on 15/02/15.
 */
public class SocketPlayer implements Runnable {
    private static final Logger sLogger = Logger.getLogger(SocketPlayer.class.getName());
    private Socket mSocket;

    public SocketPlayer(Socket socket) {
        mSocket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = mSocket.getInputStream();
            ArrayList<Integer> data = new ArrayList<>();
            while (true) {
                int value = inputStream.read();
                sLogger.fine("value = " + value);
                if (value < 0) {
                    mSocket.close();
                    return;
                }
                if (value == PacketType.TERMINATE.toValue()) {
                    doAction(data);
                    data.clear();
                    continue;
                }
                data.add(value);
            }
        } catch (IOException e) {
            sLogger.warning(e.toString());
            try {
                mSocket.close();
            } catch (IOException e1) {
                sLogger.fine(e1.toString());
            }
        }
    }

    private void send(ArrayList<Integer> data) throws IOException {
        synchronized (mSocket) {
            OutputStream outputStream = mSocket.getOutputStream();
            for (int value : data) {
                outputStream.write(value);
            }
            outputStream.write(PacketType.TERMINATE.toValue());
        }
    }

    private void doAction(ArrayList<Integer> data) throws IOException {
        PacketType type = PacketType.fromValue(data.get(0));
        switch (type) {
            case WAIT_CHALLENGER:
                doWaitChallenger(data);
                break;
            default:
                sLogger.warning(type + " is an invalid type");
        }
    }

    private void doWaitChallenger(ArrayList<Integer> data) throws IOException {
        ArrayList<Integer> sendData = new ArrayList<>();
    }
}
