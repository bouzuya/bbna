package net.bouzuya.blog;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
        Optional<Entry> newEntryOptional = getLatestEntry();
        if (!newEntryOptional.isPresent()) return;
        Entry latestEntry = newEntryOptional.get();
        String newLatestDate = latestEntry.getId().toISO8601DateString();
        Context context = this;
        BlogPreferences preferences = new BlogPreferences(context);
        Optional<String> oldEntryOptional = preferences.getLatestDate();
        preferences.setLatestDate(newLatestDate);
        if (oldEntryOptional.isPresent() && oldEntryOptional.get().equals(newLatestDate)) return;
        sendNotification(context, latestEntry);
    }

    private void sendNotification(Context context, Entry entry) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("latest_date", entry.getId().toISO8601DateString());
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
                .setContentText(entry.getId().toISO8601DateString() + " " + entry.getTitle())
                .setContentTitle("new entry: ")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        NotificationManager notificationManager =
                (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 0;
        notificationManager.notify(notificationId, notification);
    }

    private Optional<Entry> getLatestEntry() {
        Result<List<Entry>> result = new EntryListRequest().send();
        if (!result.isOk()) return Optional.empty();
        List<Entry> entryList = result.getValue();
        return entryList.isEmpty() ? Optional.<Entry>empty() : Optional.of(entryList.get(0));
    }
}
