package com.itvirtuoso.pingpong2.client.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.itvirtuoso.pingpong2.R;
import com.itvirtuoso.pingpong2.client.model.GameConnection;
import com.itvirtuoso.pingpong2.client.model.GameListener;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getName();
    private GameConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TitleFragment())
                    .commit();
        }
    }

    public void connectAsDefender() {
        GameListener listener = new AbstractGameListener() {
            @Override
            public void onConnectionSuccess() {
                waitChallenger();
            }
        };
        connect(listener);
    }


    public void connectAsChallenger(final int gameId) {
        GameListener listener = new AbstractGameListener() {
            @Override
            public void onConnectionSuccess() {
                challenge(gameId);
            }
        };
        connect(listener);
    }

    private void connect(GameListener listener) {
        mConnection = GameConnection.createInstance();
        String host = getString(R.string.server_host);
        int port = Integer.parseInt(getString(R.string.server_port));
        mConnection.connect(host, port, listener);
    }

    public void swing() {
        mConnection.swing();
    }

    private abstract class AbstractGameListener implements GameListener {
        @Override
        public void onConnectionFail(Exception e) {
            Log.e(TAG, "connection fail", e);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("エラー")
                    .setMessage("接続に失敗しました")
                    .create().show();
        }

        @Override
        public void onSendFail(Exception e) {
            Log.e(TAG, "send fail", e);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("エラー")
                    .setMessage("パケット送信に失敗しました")
                    .create().show();
        }

        @Override
        public void onReceiveFail(Exception e) {
            Log.e(TAG, "receive fail", e);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("エラー")
                    .setMessage("パケット受信に失敗しました")
                    .create().show();
        }

        public void waitChallenger() {
            mConnection.waitChallenger();
        }

        @Override
        public void onWaitChallenger(int gameId) {
            Log.d(TAG, "game id = " + gameId);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("情報")
                    .setMessage("ゲームIDは \"" + gameId + "\"です")
                    .create().show();
        }

        public void challenge(int gameId) {
            mConnection.challenge(gameId);
        }

        @Override
        public void onChallengeSuccess() {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("情報")
                    .setMessage("対戦を開始します")
                    .create().show();
        }

        @Override
        public void onChallengeFail() {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("情報")
                    .setMessage("対戦を開始できませんでした")
                    .create().show();
        }

        @Override
        public void onReady() {
            Log.d(TAG, "onReady");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RacketFragment fragment = RacketFragment.newInstance();
            transaction.add(R.id.container, fragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        @Override
        public void onSwingFail() {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("情報")
                    .setMessage("スイングに失敗しました")
                    .create().show();
        }

        @Override
        public void onServe() {
            Log.d(TAG, "onServe");
        }

        @Override
        public void onFirstBound() {
            Log.d(TAG, "onFirstBound");
        }

        @Override
        public void onSecondBound() {
            Log.d(TAG, "onSecondBound");
        }
    }
}
