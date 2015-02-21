package com.itvirtuoso.pingpong2.client.model;

import android.os.Handler;
import android.util.Log;

import com.itvirtuoso.pingpong2.common.GameUtil;
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
        service.shutdown();
    }

    private void sendPacket(PacketType type) throws IOException {
        sendPacket(type, new ArrayList<Integer>());
    }

    private void sendPacket(PacketType type, List<Integer> data) throws IOException {
        synchronized (mSocket) {
            OutputStream stream = mSocket.getOutputStream();
            stream.write(type.toValue());
            for (int value : data) {
                stream.write(value);
            }
            stream.write(PacketType.TERMINATE.toValue());
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
        service.shutdown();
    }

    public void challenge(final int gameId) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new GameRunner(mHandler) {
            @Override
            public void execute() throws IOException {
                sendPacket(PacketType.CHALLENGE, GameUtil.fromId(gameId));
            }

            @Override
            public void onException(Exception e) {
                mListener.onChallengeFail();
            }
        });
        service.shutdown();
    }

    public void swing() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new GameRunner(mHandler) {
            @Override
            public void execute() throws IOException {
                sendPacket(PacketType.SWING);
            }

            @Override
            public void onException(Exception e) {
                mListener.onSwingFail();
            }
        });
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
        Log.d(TAG, "Packet Type = " + type + ", data = [" + StringUtils.join(data, ", ") + "]");
        switch (type) {
            case ON_WAIT_CHALLENGER:
                doOnWaitChallenger(data);
                break;
            case ON_CHALLENGE_SUCCESS:
                doOnChallengeSuccess(data);
                break;
            case ON_CHALLENGE_FAIL:
                doOnChallengeFail(data);
                break;
            case ON_READY:
                doOnReady(data);
                break;
            case ON_SERVE:
                doOnServe(data);
                break;
            default:
                Log.w(TAG, type + " is an invalid type");
        }
    }

    private void doOnWaitChallenger(List<Integer> data) {
        final int gameId = GameUtil.toId(data.subList(0, 2));
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onWaitChallenger(gameId);
            }
        });
    }

    private void doOnChallengeSuccess(List<Integer> data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onChallengeSuccess();
            }
        });
    }

    private void doOnChallengeFail(List<Integer> data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onChallengeFail();
            }
        });
    }

    private void doOnReady(List<Integer> data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onReady();
            }
        });
    }

    private void doOnServe(List<Integer> data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onServe();
            }
        });
    }
}
