package com.itvirtuoso.pingpong2.client.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
                    .add(R.id.container, new PlaceholderFragment())
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
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            RacketFragment fragment = RacketFragment.newInstance();
            transaction.add(R.id.container, fragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private MainActivity mActivity;
        private Button mConnectButton;
        private Button mChallengeButton;

        public PlaceholderFragment() {
            /* Required empty public constructor */
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mConnectButton = (Button) rootView.findViewById(R.id.connect_button);
            mConnectButton.setOnClickListener(new ConnectButtonClickListener());
            mChallengeButton = (Button) rootView.findViewById(R.id.challenge_button);
            mChallengeButton.setOnClickListener(new ChallengeButtonClickListener());
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mActivity = (MainActivity) activity;
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mActivity = null;
        }

        private class ConnectButtonClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                mActivity.connectAsDefender();
            }
        }

        private class ChallengeButtonClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                Context context = getView().getContext();
                final EditText gameIdEdit = new EditText(context);
                new AlertDialog.Builder(context)
                        .setTitle("入力")
                        .setView(gameIdEdit)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int gameId = Integer.parseInt(gameIdEdit.getText().toString().trim());
                                mActivity.connectAsChallenger(gameId);
                            }
                        }).setNegativeButton("キャンセル", null)
                        .create().show();
            }
        }
    }
}
