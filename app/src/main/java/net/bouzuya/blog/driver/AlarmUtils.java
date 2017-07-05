package net.bouzuya.blog.driver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmUtils {
    private AlarmUtils() {
        throw new AssertionError();
    }

    public static void setAlarm(Context context) {
        Intent serviceIntent = new Intent(context, MainService.class);
        serviceIntent.setAction(MainService.ACTION_FETCH_ENTRY_LIST);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }
}
