package com.itvirtuoso.pingpong2.client.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itvirtuoso.pingpong2.R;

public class RacketFragment extends Fragment {
    private static final String TAG = RacketFragment.class.getName();
    private MainActivity mActivity;

    public static RacketFragment newInstance() {
        RacketFragment fragment = new RacketFragment();
        return fragment;
    }

    public RacketFragment() {
        /* Required empty public constructor */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /* nop */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_racket, container, false);
        rootView.setOnClickListener(new RacketClickListener());
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

    private class RacketClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "click racket");
            mActivity.swing();
        }
    }
}
