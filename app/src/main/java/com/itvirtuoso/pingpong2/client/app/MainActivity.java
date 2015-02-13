package com.itvirtuoso.pingpong2.client.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.itvirtuoso.pingpong2.R;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getName();

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
                /* TOOD */
            }
        }
    }
}
