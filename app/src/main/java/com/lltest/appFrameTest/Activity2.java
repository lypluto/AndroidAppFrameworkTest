package com.lltest.appFrameTest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {

    static private final String TAG = "Activity2";
    public static final String ACT_2_INFO_KEY = "infoFromAct2";

    private Context mContext;

    private StringBuilder mSb;

    private Button mAct1;
    private TextView mTxtDebug1;
    private TextView mTxtDebug2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        mContext = this;

        // UI components linking:
        mAct1 = (Button) findViewById(R.id.act1_btn);
        mTxtDebug1 = (TextView) findViewById(R.id.txt_debug_2_1);
        mTxtDebug2 = (TextView) findViewById(R.id.txt_debug_2_2);

        mAct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mAct1 is clicked");
                goToMainActivity();
            }
        });

        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

    }

    private void goToMainActivity() {
        String infoStr = "Activity2 is DONE.";
        Bundle sendBundle = new Bundle();
        sendBundle.putString(ACT_2_INFO_KEY, infoStr);

        Intent intent = new Intent(Activity2.this, MainActivity.class);      // new intent from
        // act 2 to main;
        intent.putExtras(sendBundle);    // add bundle to the intent
        startActivity(intent);
        finish();     // finish act 2
    }

    // Update UI:
    private void init() {
        Bundle receiveBundle = this.getIntent().getExtras();    // get the bundle from the intent that starts this activity.
        final String infoFromAct1 = receiveBundle.getString(MainActivity.ACT_1_INFO_KEY);

        updateDebugLog1(infoFromAct1);
    }

    private void updateDebugLog1(final String inStr) {
        if (mTxtDebug1 == null) {
            return;
        }

        // always update new log on top:
        String existingStr = (String) mTxtDebug1.getText();     // cache
        mSb = new StringBuilder("");
        mSb.append("\n").append(inStr).append("\n\n").append(existingStr);

        // Update UI:
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTxtDebug1.setText(mSb.toString());
            }
        });
    }

    private void updateDebugLog2(final String inStr) {
        if (mTxtDebug2 == null) {
            return;
        }
        String existingStr = (String) mTxtDebug2.getText();
        mSb = new StringBuilder("");
        mSb.append("\n").append(inStr).append("\n").append(existingStr);

        // Update UI:
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTxtDebug2.setText(mSb.toString());
            }
        });
    }
}
