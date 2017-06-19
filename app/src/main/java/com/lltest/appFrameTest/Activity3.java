package com.lltest.appFrameTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Activity3 extends AppCompatActivity {
    private static final String TAG = "Activity3";
    public static final String ACT_3_RESULT_BUNDLE = "resultBundleFrom3";
    public static final String ACT_3_RESULT_STR = "resultStrFrom3";

    private Button mFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        mFinish = (Button) findViewById(R.id.finish3_btn);

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mAct1 is clicked");
                finishWithResult();
            }
        });

        init();
    }

    //act3_txt_info_1

    // Update UI:
    private void init() {
        /*
        Bundle receiveBundle = this.getIntent().getExtras();
        final String infoFromAct1 = receiveBundle.getString(MainActivity.ACT_1_INFO_KEY);

        updateDebugLog1(infoFromAct1);
        */
    }

    private void finishWithResult() {
        /*
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        */

        String infoStr = "Activity 3 result string";
        Bundle sendBundle = new Bundle();
        sendBundle.putString(ACT_3_RESULT_STR, infoStr);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(ACT_3_RESULT_BUNDLE, sendBundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
