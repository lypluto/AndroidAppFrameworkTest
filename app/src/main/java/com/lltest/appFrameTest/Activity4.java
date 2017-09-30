package com.lltest.appFrameTest;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.lltest.util.GeneralUtil;

import java.util.Set;

/**
 * This activity is used to test deep link
 */

public class Activity4 extends Activity2 {

    static private final String TAG = "Activity4";

    private Uri mIntentData;
    private String mIntentScheme;
    private String mIntentHost;
    private String mIntentPath;
    private String mDataString;

    private TextView mDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        mDataText = (TextView) findViewById(R.id.data_txt);

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

        if ("lltest".equals(mIntentScheme) &&
                "lltest".equals(mIntentHost)) {
            Log.i(TAG, "Received data from intent.");
            // chrome case: fetch the parameter names from intent data:
            Set<String> intentParameterNames = mIntentData.getQueryParameterNames();
            if (intentParameterNames.isEmpty()) {
                Log.e(TAG, "No parameter from intent.");
            }
            // iterate intent parameter names, and fetch VCO Payment Request.
            // no need to do decode again; getQueryParameter will return decoded json string.
            for (String name : intentParameterNames) {
                if ("data".equals(name)) {
                    mDataString = mIntentData.getQueryParameter(name);
                }
            }
            Log.i(TAG, "data string: " + mDataString);

            if (!TextUtils.isEmpty(mDataString) && mDataText != null) {
                mDataText.setText("Data = " + mDataString);
            } else {
                mDataText.setText("No data!");
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop()");
        super.onStop();

        //onRemoveFromRecents();
        //Log.v(TAG, "calling exitApplication");
        //ExitActivity.exitApplication(this);
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause()");
        super.onPause();
    }

    public void onRemoveFromRecents() {
        Log.v(TAG, "onRemoveFromRecents()");
        // The document is no longer needed; remove its task.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!GeneralUtil.checkClickValidate()) {
                    Log.v(TAG, "click too fast, ignore");
                    Toast.makeText(getApplicationContext(), "Please click slowly.", Toast.LENGTH_LONG)
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
