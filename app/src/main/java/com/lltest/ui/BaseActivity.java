package com.lltest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.lltest.util.Constants;
import com.lltest.util.GeneralUtil;

/**
 * Created by louis.lu on 5/16/18.
 */

public class BaseActivity extends Activity {

    private static final String TAG = Constants.APP_PREFIX + BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
                    GeneralUtil.showLongToast(BaseActivity.this, "Please click slowly.");
                    return false;
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
