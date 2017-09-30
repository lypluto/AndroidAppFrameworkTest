package com.lltest.appFrameTest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lltest.util.GeneralUtil;

import java.util.ArrayList;
import java.util.List;

public class Activity2 extends AppCompatActivity {

    static private final String TAG = "Activity2";
    public static final String ACT_2_INFO_KEY = "infoFromAct2";

    private Context mContext;

    private StringBuilder mSb;

    private Button mAct1;
    private Button mBtnClickTest;
    private Button mBtnClearInfo;

    private TextView mTxtDebug1;
    private TextView mTxtDebug2;
    private List<TextView> mTxtViewList;

    // deep link test:
    /*
    private Uri mIntentData;
    private String mIntentScheme;
    private String mIntentHost;
    private String mIntentPath;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Log.v(TAG, "Activity2 onCreate()");

        /*
        Intent intent = getIntent();
        if (intent != null) {
            Log.v(TAG, "intent.getData(): " + String.valueOf(intent.getData()));
            mIntentData = intent.getData();
            if (null != mIntentData) {
                Log.w(TAG, "fetch info from intent data.");
                mIntentScheme = mIntentData.getScheme();
                Log.w(TAG, "mIntentScheme: " + mIntentScheme);
                mIntentHost = mIntentData.getHost();
                Log.w(TAG, "mIntentHost: " + mIntentHost);
                mIntentPath = mIntentData.getPath();
                Log.w(TAG, "mIntentPath: " + mIntentPath);
            } else {
                Log.w(TAG, "null data from intent.");
            }
        }
        */

        mContext = this;

        // UI components linking:
        mAct1 = (Button) findViewById(R.id.act1_btn);
        mBtnClickTest = (Button) findViewById(R.id.click_speed_test_btn);
        mBtnClearInfo = (Button) findViewById(R.id.clear_info_1_act2);

        mTxtDebug1 = (TextView) findViewById(R.id.txt_debug_2_1);
        mTxtDebug2 = (TextView) findViewById(R.id.txt_debug_2_2);
        mTxtViewList = new ArrayList<>();
        mTxtViewList.add(mTxtDebug1);
        mTxtViewList.add(mTxtDebug2);


        mAct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mAct1 is clicked");
                goToMainActivity();
            }
        });

        // click test button:
        mBtnClickTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click test btn is clicked");
                updateDebugLog1("Button is clicked...");
            }
        });

        // clear info button:
        mBtnClearInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clear info btn is clicked");
                GeneralUtil.clearMultiTextViewInfo(Activity2.this, mTxtViewList);
            }
        });

        init();
    }

    @Override
    protected void onResume() {
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

        // update info to log 1:
        updateDebugLog1(infoFromAct1);
    }

    private void updateDebugLog1(final String inStr) {
        GeneralUtil.appendTextViewInfo(Activity2.this, mTxtDebug1, inStr);
    }

    private void updateDebugLog2(final String inStr) {
        GeneralUtil.appendTextViewInfo(Activity2.this, mTxtDebug2, inStr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!GeneralUtil.checkClickValidate()) {
                    Log.v(TAG, "click too fast, ignore");
                    Toast.makeText(getApplicationContext(), "Please click slowly.", Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
