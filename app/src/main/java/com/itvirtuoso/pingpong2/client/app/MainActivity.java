package com.itvirtuoso.pingpong2.client.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.itvirtuoso.pingpong2.R;
import com.itvirtuoso.pingpong2.client.model.Game;
import com.itvirtuoso.pingpong2.client.model.GameListener;

public class MainActivity extends ActionBarActivity implements GameListener {
    private static final String TAG = MainActivity.class.getName();
    private Game mGame;

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
        mGame = Game.createInstance(new Handler(), this);
        String host = getString(R.string.server_host);
        int port = Integer.parseInt(getString(R.string.server_port));
        mGame.connect(host, port);
    }

    @Override
    public void onConnectionFail(Exception e) {
        Log.e(TAG, "connection failed", e);
        new AlertDialog.Builder(this)
                .setTitle("エラー")
                .setMessage("接続に失敗しました")
                .create().show();
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
