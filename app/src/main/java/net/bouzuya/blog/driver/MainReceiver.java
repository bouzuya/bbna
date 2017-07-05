package net.bouzuya.blog.driver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

public class MainReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Timber.d("onReceive: ");
            AlarmUtils.setAlarm(context.getApplicationContext());
        }
    }
}
