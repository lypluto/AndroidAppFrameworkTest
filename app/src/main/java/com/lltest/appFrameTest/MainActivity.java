package com.lltest.appFrameTest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lltest.util.Constants;
import com.lltest.util.GeneralUtil;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements FragmentOne.OnFragmentInteractionListener, FragmentTwo
        .OnFragmentInteractionListener2 {

    static private final String TAG = "MainActivity";


    private final String LOAD_STR = "Loading...";
    private final String TICK_STR = "Ticking...";

    public static final String ACT_1_INFO_KEY = "infoFromAct1";

    // UI components variables:
    private Button mBtnF1;
    private Button mBtnF2;
    private Button mBtnTimer;
    private Button mBtnActivity2;
    private Button mBtnSubAct3;
    private TextView mTxtDebug1;
    private TextView mTxtDebug2;

    // count down timer test variables:
    private CountDownTimer mTimer1;
    private final int TICK_TIME = 1000;
    private final int FINISH_TIME = 10000;

    private StringBuilder mSb;

    // use fragment lock to control only one fragment can be launched (F1 or F2).
    // the lock will be released in fragment's onPause().
    private static AtomicBoolean mFragmentLock = new AtomicBoolean(false);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        mBtnF1 = (Button) findViewById(R.id.f1_btn);
        mBtnF2 = (Button) findViewById(R.id.f2_btn);

        mBtnTimer = (Button) findViewById(R.id.btn_timer);
        mBtnActivity2 = (Button) findViewById(R.id.act2_btn);
        mBtnSubAct3 = (Button) findViewById(R.id.sub_act3_btn);

        mTxtDebug1 = (TextView) findViewById(R.id.txt_debug_1);
        mTxtDebug2 = (TextView) findViewById(R.id.txt_debug_2);



        mBtnF1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnF1 is clicked");
                loadFragmentOne();
            }
        });

        mBtnF2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnF2 is clicked");
                loadFragmentTwo();
            }
        });

        mBtnActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnActivity2 is clicked");
                startActivity2();
            }
        });

        mBtnSubAct3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnSubAct3 is clicked");
                startSubAct3();
            }
        });

        mBtnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnTimer is clicked");
                mTxtDebug2.setText("");     // reset debug log2
                updateDebugLog1("Timer is started.");
                mTimer1.start();
            }
        });

        // implement the timer object.
        // once the mTimer1.start() is called, the timer starts ticking.
        // once the mTimer2.cancel() is called, the timer stops.
        mTimer1 = new CountDownTimer(FINISH_TIME, TICK_TIME) {
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "timer onTick.");
                doTimerTickTask();
            }

            public void onFinish() {
                Log.v(TAG, "timer onFinish. cancel timer");
                updateDebugLog1("Timer is finished.");
                doTimerDoneTask();
                //mTimer1.cancel();     // no need
            }
        };

        init();     // init UI

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

        Log.d(TAG, "release lock: ");
        mFragmentLock.set(false);

        Log.d(TAG, "update batt level.");
        updateDebugLog2(GeneralUtil.getBatteryStatusString(this));

    }

    /**
     * Try to start another activity from this activity
     */
    public void startActivity2() {
        String infoStr = mTxtDebug1.getText().toString();
        Bundle sendBundle = new Bundle();
        sendBundle.putString(ACT_1_INFO_KEY, infoStr);

        Intent intent = new Intent(MainActivity.this, Activity2.class);      // new intent from
        // main act to act 2;
        intent.putExtras(sendBundle);    // add bundle to the intent
        startActivity(intent);
        finish();     // finish main activity after start act 2.
    }

    /**
     * Try to start a sub-activity from this activity,
     * and waiting for the return data from the sub-activity.
     */
    public void startSubAct3() {
        Intent i = new Intent(this, Activity3.class);
        startActivityForResult(i, Constants.REQUEST_CODE);
    }

    // Fragment Operations:

    /**
     * Starting a fragment from current activity.
     * This process needs to use FragmentManager.
     * This process doesn't need to use any intent.
     */
    public void loadFragmentOne() {
        Log.v(TAG, "loadFragmentOne");
        FragmentOne fragmentOne;
        //fragmentOne = (FragmentOne) this.getSupportFragmentManager().findFragmentByTag
        // (FRAGMENT_ONE_TAG);

        //if (fragmentOne == null) {
        fragmentOne = FragmentOne.newInstance("arg1", "arg2");
        //fragmentOne.setArguments(getIntent().getExtras());

        // seems like needs to use supportFragmentManager instead of general FragmentManager...
        getSupportFragmentManager().beginTransaction()
                //this.getFragmentManager().beginTransaction()
                .add(android.R.id.content, fragmentOne, Constants.FRAGMENT_ONE_TAG)
                .commitAllowingStateLoss();
        //}
    }

    public void loadFragmentTwo() {
        Log.v(TAG, "loadFragmentTwo");
        FragmentTwo fragment2;
        //fragment2 = (FragmentTwo) this.getSupportFragmentManager().findFragmentByTag
        // (FRAGMENT_TWO_TAG);

        //if (fragment2 == null) {
        fragment2 = FragmentTwo.newInstance("arg3", "arg4");
        //fragmentOne.setArguments(getIntent().getExtras());

        // seems like needs to use supportFragmentManager instead of general FragmentManager...
        getSupportFragmentManager().beginTransaction()
                //this.getFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment2, Constants.FRAGMENT_TWO_TAG)
                .commitAllowingStateLoss();
        //}
    }

    // Timer tasks:
    private void doTimerTickTask() {
        updateDebugLog2(TICK_STR);
    }

    private void doTimerDoneTask() {
        updateDebugLog2("Timer Finish");
    }

    // UI update APIs:
    private void init() {
        StringBuilder sb = new StringBuilder("");

        // [0] display info from Act 2. If Main Act is started by Act 2, then this will be
        // displayed.
        Bundle receiveBundle2 = getIntent().getExtras(); // get the bundle from the
        // intent that starts this activity.

        if (receiveBundle2 != null) {
            final String infoFromAct1 = receiveBundle2.getString(Activity2.ACT_2_INFO_KEY);
            if (infoFromAct1 != null) {
                sb.append(infoFromAct1).append("\n");
            }
        }

        // [1] get Android version:
        String androidVer = GeneralUtil.getAndroidVersion();
        sb.append("Android: ").append(String.valueOf(androidVer)).append("\n");

        // [2] get display language:
        String currentDisplayLan = GeneralUtil.getCurrentDisplayLanguage();
        sb.append("Display Language: ").append(currentDisplayLan).append("\n");

        // [3] check if RTL is enabled:
        boolean isRtl;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isRtl = (GeneralUtil.isRTL() || GeneralUtil.isForceRTL(this));
        } else {
            isRtl = GeneralUtil.isRTL();

        }
        String isRtlStr = (isRtl) ? "Yes" : "No";
        sb.append("Is Right to Left? ").append(isRtlStr);

        // UPDATE INFO 1:
        updateDebugLog1(sb.toString());
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

    /**
     * Main Activity needs to override this onActivityResult to receive result from sub-activity.
     * This onActivityResult() cooperates with startActivityForResult().
     *
     * @param requestCode
     * @param resultCode
     * @param data        the intent returned
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_CODE) {
            String result = "No Result String";
            if (resultCode == Activity.RESULT_OK) {
                //result = data.getStringExtra(ACT_3_RESULT);
                if (data.hasExtra(Activity3.ACT_3_RESULT_BUNDLE)) {
                    final Bundle bundle3 = data.getExtras().getBundle(Activity3
                            .ACT_3_RESULT_BUNDLE);
                    if (bundle3 != null) {
                        result = bundle3.getString(Activity3.ACT_3_RESULT_STR);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                result = "Act 3 Canceled...";
            }
            updateDebugLog1(result);
        }
    }   // END of onActivityResult

    public void setFragmentLock(boolean lockStatus) {
        mFragmentLock.set(lockStatus);
    }

    // impl callbacks declared in fragments:

    @Override
    public void onFragmentInteraction(String s) {
        // do nothing
    }

    @Override
    public void onFragmentInteraction2(Uri uri) {
        // do nothing
    }
}
