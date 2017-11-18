package com.lltest.androidtestlibrary;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by louis.lu on 11/15/17.
 */

public class TimeService extends Service {

    private static final String TAG = "TimeService";

    private Messenger mServerSideMessenger = new Messenger(new TimeService.TimeServiceHandler());

    private class TimeServiceHandler extends Handler {

        @Override
        public void handleMessage(Message msgFromClient) {
            if (msgFromClient == null) {
                Log.e(TAG, "TimeServiceHandler-handleMessage: null message from client!");
                return;
            }

            PackageManager pm = getPackageManager();
            if (null == pm) {
                Log.e(TAG, "TimeServiceHandler-handleMessage: " +
                        "null PackageManager! End handleMessage.");
                return;
            }

            String[] packages = pm.getPackagesForUid(msgFromClient.sendingUid);
            if (null == packages || 0 == packages.length) {
                Log.e(TAG, "TimeServiceHandler-handleMessage: no sending package! End handleMessage.");
                return;
            }

            for (String packageName : packages) {
                Log.v(TAG, "TimeServiceHandler-handleMessage - sending packageName " + packageName);
                // TODO: check caller package signature later
                /*
                if (packageName != null &&
                        pm.checkSignatures(getPackageName(), packageName) >= 0) {
                    Log.d(TAG, "TimeServiceHandler-handleMessage: signature success!");
                } else {
                    Log.e(TAG, "TimeServiceHandler-handleMessage: signature fail! End handleMessage.");
                    return;
                }
                */
            }

            // Handle message from client:
            int clientMsgType = msgFromClient.what;
            switch (clientMsgType) {
                case TimeServiceConsts.CHECK_TIME_MSG_WHAT_STATUS_CODE:
                    Log.i(TAG, "TimeServiceHandler-handleMess: case " +
                            "CHECK_TIME_MSG_WHAT_STATUS_CODE: " + clientMsgType);
                    try {
                        Log.d(TAG, "Try to generate message...");
                        Message resp = Message.obtain(null, TimeServiceConsts.CHECK_TIME_MSG_WHAT_STATUS_RESP);

                        String dataTimeInfo = getCurrentTime();
                        Log.d(TAG, "Got dataTimeInfo: " + dataTimeInfo);

                        Bundle responseBundle = new Bundle();
                        responseBundle.putString(TimeServiceConsts.KEY_TIME, dataTimeInfo);
                        resp.setData(responseBundle);

                        if (msgFromClient.replyTo != null) {
                            Log.i(TAG, "sending resp msg back...");
                            Log.i(TAG, "replyTo name: " + msgFromClient.replyTo.getClass()
                                    .getCanonicalName());
                            Log.i(TAG, "replyTo binder name: " + msgFromClient.replyTo.getBinder().getClass()
                                    .getCanonicalName());
                            msgFromClient.replyTo.send(resp);
                        } else {
                            Log.w(TAG, "null replyTo!");
                        }
                    } catch (RemoteException e) {
                        Log.e(TAG, "RemoteException on sending time info.");
                        e.printStackTrace();
                    }
                    //Log.i(TAG, "stop myself...");
                    //stopSelf();
                    break;
                case TimeServiceConsts.CHECK_TIME_MSG_WHAT_STATUS_RESP:
                    Log.i(TAG, "TimeServiceHandler-handleMess: case " +
                            "CHECK_TIME_MSG_WHAT_STATUS_RESP: " + clientMsgType);

                    break;
                default:
                    super.handleMessage(msgFromClient);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind " + Binder.getCallingUid());

        // TODO: should we check caller signature here as well?

        if (intent != null && intent.getAction().
                equals(TimeServiceConsts.CHECK_TIME_SERVICE_ACTION)) {
            return mServerSideMessenger.getBinder();
        }
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind by: " + Binder.getCallingUid());
        return super.onUnbind(intent);
        //stopSelf();     // TODO: should we do this?
        //return result;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        // TODO: clean up
        Log.d(TAG, "onTaskRemoved");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // TODO: clean up
        Log.d(TAG, "onLowMemory");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: ...
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        // TODO: clean up
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
    }

    /**
     * Used to get current system time
     * @return
     */
    public String getCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.toString();
    }

}
