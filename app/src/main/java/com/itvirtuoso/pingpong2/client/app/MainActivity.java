package com.itvirtuoso.pingpong2.client.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.itvirtuoso.pingpong2.R;
import com.itvirtuoso.pingpong2.client.model.GameConnection;
import com.itvirtuoso.pingpong2.client.model.GameListener;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getName();
    private GameConnection mConnection;
    private GameListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public void connectAsDefender() {
        mConnection = GameConnection.createInstance();
        String host = getString(R.string.server_host);
        int port = Integer.parseInt(getString(R.string.server_port));
        mListener = new AbstractGameListener() {
            @Override
            public void onConnectionSuccess() {
                waitChallenger();
            }
        };
        mConnection.connect(host, port, mListener);
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
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private MainActivity mActivity;
        private Button mConnectButton;

        public PlaceholderFragment() {
            /* nop */
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mConnectButton = (Button) rootView.findViewById(R.id.connect_button);
            mConnectButton.setOnClickListener(new ConnectButtonClickListener());
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mActivity = (MainActivity) activity;
        }

        private class ConnectButtonClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                mActivity.connectAsDefender();
            }
        }
    }
}
