package com.squalle0nhart.hoctienganh.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.squalle0nhart.hoctienganh.receiver.ScreenReceiver;

/**
 * Created by squalle0nhart on 19/4/2017.
 */

public class LockScreenService extends Service {
    ScreenReceiver mScreenReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mScreenReceiver == null) {
            mScreenReceiver = new ScreenReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.setPriority(999);
            registerReceiver(mScreenReceiver, intentFilter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mScreenReceiver != null) {
            unregisterReceiver(mScreenReceiver);
        }
    }
}
