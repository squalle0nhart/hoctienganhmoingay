package com.squalle0nhart.hoctienganh.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.squalle0nhart.hoctienganh.ui.activity.LockScreenActivity;

/**
 * Created by squalle0nhart on 31/3/2017.
 */

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent lockIntent = new Intent(context, LockScreenActivity.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(lockIntent);
    }
}
