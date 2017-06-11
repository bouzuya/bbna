package net.bouzuya.blog;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import net.bouzuya.blog.models.Entry;
import net.bouzuya.blog.models.Optional;
import net.bouzuya.blog.models.Result;
import net.bouzuya.blog.requests.EntryListRequest;
import net.bouzuya.blog.views.activities.MainActivity;

import java.util.List;

public class MainService extends IntentService {
    public static final String ACTION_FETCH_ENTRY_LIST =
            "net.bouzuya.blog.action.FETCH_ENTRY_LIST";

    public MainService() {
        super("MainService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        switch (action) {
            case ACTION_FETCH_ENTRY_LIST:
                handleActionFetchEntryList();
                return;
            default:
                // do nothing
        }
    }

    private void handleActionFetchEntryList() {
        Entry latestEntry = getLatestEntry();
        if (latestEntry == null) return;
        String newLatestDate = latestEntry.getDate();
        Context context = this;
        BlogPreferences preferences = new BlogPreferences(context);
        Optional<String> oldLatestDateOptional = preferences.getLatestDate();
        preferences.setLatestDate(newLatestDate);
        if (newLatestDate != null &&
                (!oldLatestDateOptional.isPresent() ||
                        !oldLatestDateOptional.get().equals(newLatestDate))) {
            sendNotification(context, latestEntry);
        }
    }

    private void sendNotification(Context context, Entry entry) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("latest_date", entry.getDate());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        int requestCode = 0;
        int flags = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, requestCode, intent, flags
        );
        Notification notification = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentText(entry.getDate() + " " + entry.getTitle())
                .setContentTitle("new entry: ")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        NotificationManager notificationManager =
                (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 0;
        notificationManager.notify(notificationId, notification);
    }

    @Nullable
    private Entry getLatestEntry() {
        Result<List<Entry>> result = new EntryListRequest().send();
        if (!result.isOk()) return null;
        List<Entry> entryList = result.getValue();
        return entryList.isEmpty() ? null : entryList.get(0);
    }
}
