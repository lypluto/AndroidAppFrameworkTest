package com.lltest.appFrameTest;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.lltest.ui.LoginActivity;
import com.lltest.util.Constants;
import com.lltest.util.GeneralUtil;
import com.lltest.util.JsonUtil;
import com.lltest.util.LogUtil;
import com.lltest.util.NetworkUtil;
import com.lltest.util.ReminderConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.lltest.util.Constants.DUMMY_TEST_JSON_STRING;

public class MainActivity extends AppCompatActivity implements
        FragmentOne.OnFragmentInteractionListener,
        FragmentTwo.OnFragmentInteractionListener2,
        Fragment3.OnFragmentInteractionListener3,
        FragmentSharedPrefsTest.OnFragmentInteractionListenerSharedPrefs {

    static private final String TAG = Constants.APP_PREFIX + "MainActivity";


    private final String LOAD_STR = "Loading...";
    private final String TICK_STR = "Ticking...";

    public static final String ACT_1_INFO_KEY = "infoFromAct1";

    // UI components variables:
    private View mRootView;
    private Button mBtnF1, mBtnF2, mBtnActivity2, mBtnSubAct3, mBtnSharedPrefsTest, mBtnWifiTest;
    private Button mBtnClearInfo;
    private Button mBtnF3;
    private Button mBtnViewPager;
    private Button mBtnLogin;

    private Button mBtnDoAlias, mBtnClearAlias;

    private Switch mSwitchTimer;
    private TextView mTxtDebug1, mTxtDebug2;
    private List<TextView> mTxtViewList;

    // count down timer test variables:
    private CountDownTimer mTimer1;
    private final int TICK_TIME = 1000;
    private final int FINISH_TIME = 5000;   // 5 sec

    private StringBuilder mSb;

    // use fragment lock to control only one fragment can be launched (F1 or F2).
    // the lock will be released in fragment's onPause().
    private static AtomicBoolean mFragmentLock = new AtomicBoolean(false);

    private MiniGestureDetector gestureDetector;
    private GestureDetectorCompat detector;

    private WifiReceiver mWifiReceiver = new WifiReceiver();

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info != null && info.isConnected()) {
                // Do your work.
                Log.d(TAG, "isConnected " + info.isConnected());

                // e.g. To check the Network Name or other info:
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();
                Log.d(TAG, "ssid " + ssid);

                int numberOfLevels = ReminderConstants.WIFI_STRENGTH_RANGE;
                int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                Log.d(TAG, "level " + level);
                updateDebugLog1("ssid: " + ssid + ", level: " + level);
                //GeneralUtil.showShortToast(context, "ssid: " + ssid + ", level: " + level);

                // ssid example: "S-B2B-LAB1", "S-B2B-LAB2"
            } else if (info != null && !info.isConnected()) {
                Log.d(TAG, "isConnected " + info.isConnected());
                updateDebugLog1("WIFI disconnected!");
                //GeneralUtil.showShortToast(context, "wifi disconnected!");
            }
        }
    }

    private BroadcastReceiver myRssiChangeReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context ctx, Intent intent) {
            int newRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0);
            Log.d(TAG, "newRssi: " + newRssi);
            //updateDebugLog1(String.valueOf(newRssi));
            String wifiSsid = NetworkUtil.getWifiSSID(MainActivity.this);
            int wifiLevel = NetworkUtil.getWifiStrengthLevel(MainActivity.this);

            StringBuilder sb = new StringBuilder();
            sb.append("WIFI SSID: ").append(wifiSsid).append("\n").append("WIFI STRENGTH: ")
                    .append(String.valueOf(wifiLevel));
            updateDebugLog1(sb.toString());
            //GeneralUtil.showShortToast(ctx, sb.toString());
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("Test App 1");
        setContentView(R.layout.activity_main);

        mRootView = findViewById(R.id.activity_main);

        Log.d(TAG, "MainActivity onCreate()");

        // In OnCreate or custome view constructor (which extends one of Android views)
        //detector = new GestureDetectorCompat(getApplicationContext(), gestureDetector);

        initUI();           // init UI components
        initPart2();        // register receivers, init global variables

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "unregister broadcast receivers.");
        /*
        // TODO: recover this when need detect wifi status
        unregisterReceiver(mWifiReceiver);
        unregisterReceiver(myRssiChangeReceiver);
        */
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
     * Try to start view pager test activity from this activity
     */
    public void startViewPagerActivity() {
        String infoStr = mTxtDebug1.getText().toString();
        Bundle sendBundle = new Bundle();
        sendBundle.putString(ACT_1_INFO_KEY, infoStr);

        Intent intent = new Intent(MainActivity.this, ViewPagerActivity.class);      // new intent from
        // main act to act 2;
        intent.putExtras(sendBundle);    // add bundle to the intent
        startActivity(intent);
        finish();     // finish main activity after start new activity.
    }

    public void startLoginActivity() {
        String infoStr = mTxtDebug1.getText().toString();
        Bundle sendBundle = new Bundle();
        sendBundle.putString(ACT_1_INFO_KEY, infoStr);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);      // new intent from
        // main act to act 2;
        intent.putExtras(sendBundle);    // add bundle to the intent
        startActivity(intent);
        finish();     // finish main activity after start new activity.
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

    public void loadFragment3() {
        Log.v(TAG, "loadFragment3");
        Fragment3 fragment3;
        fragment3 = Fragment3.newInstance("arg1", "arg2");
        getSupportFragmentManager().beginTransaction()
                //this.getFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment3, Constants.FRAGMENT_TWO_TAG)
                .commitAllowingStateLoss();
    }

    public void loadFragmentSharedPrefsTest() {
        Log.v(TAG, "loadFragmentSharedPrefsTest");
        FragmentSharedPrefsTest fragmentSharedPrefs;
        fragmentSharedPrefs = FragmentSharedPrefsTest.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, fragmentSharedPrefs, Constants.FRAGMENT_SHARED_PREFS_TAG)
                .commitAllowingStateLoss();

    }

    // Timer tasks:
    private void doTimerTickTask() {
        updateDebugLog2(TICK_STR);
    }

    private void doTimerDoneTask() {
        updateDebugLog2("Timer Finish");
    }

    /**
     * Initialize UI
     *
     */
    private void initUI() {
        mBtnClearInfo = (Button) findViewById(R.id.btn_clear_info_1_act1);
        mBtnF1 = (Button) findViewById(R.id.f1_btn);
        mBtnF2 = (Button) findViewById(R.id.f2_btn);
        mBtnF3 = (Button) findViewById(R.id.f3_btn);
        mBtnActivity2 = (Button) findViewById(R.id.act2_btn);
        mBtnSubAct3 = (Button) findViewById(R.id.sub_act3_btn);
        mBtnSharedPrefsTest = (Button) findViewById(R.id.btnSharedPrefsTest);
        mBtnWifiTest = (Button) findViewById(R.id.btnWifiTest);
        mBtnDoAlias = (Button) findViewById(R.id.btnDoAlias);
        mBtnClearAlias = (Button) findViewById(R.id.btnClearAlias);
        mBtnViewPager = (Button) findViewById(R.id.btnViewPager);

        mSwitchTimer = (Switch) findViewById(R.id.timer_switch);

        mBtnLogin = findViewById(R.id.btnLogin);

        mTxtDebug1 = (TextView) findViewById(R.id.txt_debug_1);
        mTxtDebug2 = (TextView) findViewById(R.id.txt_debug_2);
        mTxtViewList = new ArrayList<>();
        mTxtViewList.add(mTxtDebug1);
        mTxtViewList.add(mTxtDebug2);

        // clear info button:
        mBtnClearInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clear info btn is clicked");
                GeneralUtil.clearMultiTextViewInfo(MainActivity.this, mTxtViewList);
            }
        });

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

        mBtnF3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnF3 is clicked");
                loadFragment3();
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

        mBtnSharedPrefsTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnSharedPrefsTest is clicked");
                loadFragmentSharedPrefsTest();
            }
        });

        mBtnWifiTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnWifiTest is clicked");
                String wifiSsid = NetworkUtil.getWifiSSID(MainActivity.this);
                int wifiLevel = NetworkUtil.getWifiStrengthLevel(MainActivity.this);

                StringBuilder sb = new StringBuilder();
                sb.append("WIFI SSID: ").append(wifiSsid).append("\n").append("WIFI STRENGTH: ")
                        .append(String.valueOf(wifiLevel));
                updateDebugLog1(sb.toString());
            }
        });

        mBtnDoAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnDoAlias is clicked");
                doAlias(v);
            }
        });

        mBtnClearAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnClearAlias is clicked");
                clearAlias(v);
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
                mSwitchTimer.setChecked(false); // Timer finished, reset switch status
                //mTimer1.cancel();     // no need
            }
        };

        mSwitchTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitchTimer.isChecked()) {
                    Log.d(TAG, "timer switch is on");
                    GeneralUtil.clearTextViewInfo(MainActivity.this, mTxtDebug2);
                    updateDebugLog1("Timer is started.");
                    mTimer1.start();
                } else {
                    Log.d(TAG, "timer switch is off");
                    GeneralUtil.clearTextViewInfo(MainActivity.this, mTxtDebug2);
                    mTimer1.cancel();
                    //doTimerDoneTask();
                    updateDebugLog1("Timer is cancelled.");
                }
            }
        });

        mBtnViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnViewPager is clicked");
                startViewPagerActivity();
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnF1 is clicked");
                startLoginActivity();
            }
        });
    }

    // initialize:
    private void initPart2() {

        // register broadcast receivers:
        // TODO: recover this when need detect wifi status
        /*
        this.registerReceiver(this.mWifiReceiver,
                new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
        this.registerReceiver(this.myRssiChangeReceiver,
                new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
                */

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
        sb.append("Is Right to Left? ").append(isRtlStr).append("\n");

        // [4] logic checking is one-handed mode enabled or not:
        /*
        try {
            int oneHandFlag = Settings.System.getInt(getContentResolver(), Settings.System
                    .SEM_ONE_HAND_ANY_SCREEN_RUNNING);
            Log.d(TAG, "one-hand mode flag: " + oneHandFlag);
            if (oneHandFlag == 1) {
                sb.append("One-hand mode? ").append("YES").append("\n");
            } else {
                sb.append("One-hand mode? ").append("NO").append("\n");
            }
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "NO one-hand mode setting!");
        }*/

        // [5] json parsing test:
        String testMsgBodyString = DUMMY_TEST_JSON_STRING;

        int statusCode = JsonUtil.getInnerStatusFromMsgBody(testMsgBodyString);
        JSONObject body = JsonUtil.getInnerBodyFromMsgBody(testMsgBodyString);
        String reason = JsonUtil.getInnerReasonFromMsgBody(testMsgBodyString);
        String extra = JsonUtil.getInnerExtraFromMsgBody(testMsgBodyString);

        LogUtil.v(TAG, "status: " + statusCode);
        LogUtil.v(TAG, "body: " + String.valueOf(body));
        LogUtil.v(TAG, "reason: " + reason);
        LogUtil.v(TAG, "extra: " + extra);


        // UPDATE INFO 1:
        updateDebugLog1(sb.toString());
    }

    private void updateDebugLog1(final String inStr) {
        GeneralUtil.appendTextViewInfo(MainActivity.this, mTxtDebug1, inStr);
    }

    private void updateDebugLog2(final String inStr) {
        GeneralUtil.appendTextViewInfo(MainActivity.this, mTxtDebug2, inStr);
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



    /**
     * Callback detects full/half screen mode change event.
     *
     * @param isInMultiWindowMode
     */
    @Override
    public void onMultiWindowModeChanged (boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        Log.v(TAG, "onMultiWindowModeChanged: " + isInMultiWindowMode);
        if (isInMultiWindowMode) {
            GeneralUtil.showLongToast(MainActivity.this, "Multiple Window Mode");
        } else {
            GeneralUtil.showLongToast(MainActivity.this, "Full Screen Mode");
        }
    }

    /**
     * Check click validation in dispatchTouchEvent,
     * so that too fast click events can be avoided.
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!GeneralUtil.checkClickValidate()) {
                    Log.v(TAG, "click too fast, ignore");
                    GeneralUtil.showLongToast(MainActivity.this, "Please click slowly.");
                    return false;
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    private void doAlias(View v){
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, "com.lltest.appFrameTest.MainAliasActivity"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        GeneralUtil.showShortToast(MainActivity.this, "Please close app.");

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolves = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }
    }

    private void clearAlias(View v){
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, "com.lltest.appFrameTest.MainActivity"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        GeneralUtil.showShortToast(MainActivity.this, "Please close app.");

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolves = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }
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

    @Override
    public void onFragmentInteraction3(Uri uri) {

    }

    @Override
    public void OnFragmentInteractionListenerSharedPrefs(Uri uri) {
        // do nothing
    }


    class MiniGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > Constants.SWIPE_MAX_OFF_PATH){
                    return false;
                }
                // right to left swipe
                if (e1.getX() - e2.getX() > Constants.SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
                    onLeftSwipe();
                }
                // left to right swipe
                else if (e2.getX() - e1.getX() > Constants.SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
                    onRightSwipe();
                }
            } catch (Exception e) {

            }
            return false;
        }
    }

    private void onLeftSwipe() {
        Log.d(TAG, "swipe left...");
    }

    private void onRightSwipe() {
        Log.d(TAG, "swipe right...");
    }


}
