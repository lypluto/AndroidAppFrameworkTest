package com.lltest.appFrameTest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lltest.androidtestlibrary.TimeServiceConsts;
import com.lltest.common.CommonClass;
import com.lltest.util.Constants;
import com.lltest.util.GeneralUtil;

import java.util.ArrayList;
import java.util.List;

import static com.lltest.util.GeneralUtil.showMessage;

public class Activity2 extends AppCompatActivity {

    static private final String TAG = Constants.APP_PREFIX + "Activity2";
    public static final String ACT_2_INFO_KEY = "infoFromAct2";

    private Context mContext;

    private Messenger mService = null;      // binder of server side Messenger
    private boolean isServiceBound = false;
    private ServiceConnection mTimeServiceConnection = null;

    private StringBuilder mSb;

    private Button mAct1, mBtnClickTest, mBtnClearInfo, mBtnShowTime, mBtnKillTime;

    private Switch mSwitchTimeService;

    private TextView mTxtDebug1;
    private TextView mTxtDebug2;
    private List<TextView> mTxtViewList;

    private CommonClass myClass;

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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

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
        mBtnShowTime = (Button) findViewById(R.id.show_time_btn);
        mBtnKillTime = (Button) findViewById(R.id.kill_time_service_btn);

        mSwitchTimeService = (Switch) findViewById(R.id.time_service_switch);

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

        mSwitchTimeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitchTimeService.isChecked()) {
                    Log.d(TAG, "time service switch is on; try to bind time service.");
                    bindTimeService();
                } else {
                    Log.d(TAG, "time service switch is off; try to unbind time service.");
                    unbindTimeService();
                }
            }
        });

        mBtnShowTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnShowTime is clicked");
                if (isServiceBound) {
                    // send msg to Time Service, and fetch current time
                    //Message message = new Message();
                    //message.what = TimeServiceConsts.CHECK_TIME_MSG_WHAT_STATUS_CODE;
                    Message message = Message.obtain(null, TimeServiceConsts.CHECK_TIME_MSG_WHAT_STATUS_CODE);
                    //message.arg1 = 0;
                    //message.arg2 = 0;
                    //message.obj = null;

                    if (null != mClientSideMessenger) {
                        message.replyTo = mClientSideMessenger;     // assign client side messenger to replyTo.
                    } else {
                        Log.w(TAG, "No mClientSideMessenger to replyTo.");
                        showMessage(Activity2.this, "Time", "No client side messenger.");
                        return;
                    }

                    try {
                        Log.d(TAG, "try to send request message to time service.");
                        if (null == mService) {
                            Log.d(TAG, "no service side binder; end onClick.");
                            return;
                        }
                        mService.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.w(TAG, "No time service now.");
                    showMessage(Activity2.this, "Time", "No service now...");
                }
            }
        });

        mBtnKillTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mBtnKillTime is clicked");
                Intent serviceIntent = new Intent(TimeServiceConsts.CHECK_TIME_SERVICE_ACTION);
                Log.d(TAG, "getPackageName: " + getPackageName());
                serviceIntent.setPackage(getPackageName());
                Log.d(TAG, "try to stop service.");
                stopService(serviceIntent);
            }
        });

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindTimeService();
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

    private void bindTimeService() {
        Log.d(TAG, "bindTimeService");
        Intent serviceIntent = new Intent(TimeServiceConsts.CHECK_TIME_SERVICE_ACTION);
        Log.d(TAG, "getPackageName: " + getPackageName());
        serviceIntent.setPackage(getPackageName());
        //startService(serviceIntent);

        // init connection:
        if (null == mTimeServiceConnection) {
            mTimeServiceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder binder) {
                    Log.d(TAG, "#onServiceConnected");
                    isServiceBound = true;
                    mService = new Messenger(binder);   // instantiate Messenger with binder.
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.d(TAG, "#onServiceDisconnected");
                    isServiceBound = false;     // reset flag
                    mService = null;    // clear binder
                    mTimeServiceConnection = null;
                    // update UI:
                    if (null != mSwitchTimeService) {
                        mSwitchTimeService.setChecked(false);
                    }
                }
            };
        }

        Log.d(TAG, "call bindService");
        boolean bindResult = bindService(serviceIntent, mTimeServiceConnection,
                Context.BIND_AUTO_CREATE);
        Log.v(TAG, "bindResult: " + bindResult);
    }


    private void unbindTimeService() {
        Log.v(TAG, "inside unbindTimeService");
        // Unbind service to release occupy
        if (isServiceBound) {
            Log.v(TAG, "call unbindService");
            /*
            Intent serviceIntent = new Intent("com.lltest.timeservice.CHECK_TIME_SERVICE");
            serviceIntent.setPackage("com.lltest.androidappframeworktest");
            stopService(serviceIntent);
            */
            if (null != mTimeServiceConnection) {
                unbindService(mTimeServiceConnection);
                mTimeServiceConnection = null;      // reset connection instance
            }

            isServiceBound = false;       //TODO: safe and necessary?
            mService = null;
        }
    }

    private Messenger mClientSideMessenger = new Messenger(new TimeServiceRespHandler());

    private class TimeServiceRespHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                Log.e(TAG, "TimeServiceRespHandler-handleMess: null msg!");
                super.handleMessage(msg);
                return;
            }

            // Handle message from caller:
            int msgType = msg.what;
            Log.i(TAG, "TimeServiceRespHandler-handleMess: msgType " + msgType);

            switch (msgType) {
                case TimeServiceConsts.CHECK_TIME_MSG_WHAT_STATUS_RESP:
                    Log.i(TAG, "TimeServiceRespHandler-handleMess: case " +
                            "CHECK_TIME_MSG_WHAT_STATUS_RESP");
                    Bundle bundle = msg.getData();
                    String timeInfo = bundle.getString(TimeServiceConsts.KEY_TIME);
                    showMessage(Activity2.this, "Time", timeInfo);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
