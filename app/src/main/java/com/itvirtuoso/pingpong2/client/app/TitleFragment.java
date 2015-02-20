package com.itvirtuoso.pingpong2.client.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.itvirtuoso.pingpong2.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TitleFragment extends Fragment {
    private MainActivity mActivity;
    private Button mConnectButton;
    private Button mChallengeButton;

    public TitleFragment() {
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
