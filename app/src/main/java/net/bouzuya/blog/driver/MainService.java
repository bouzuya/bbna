package net.bouzuya.blog.driver;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import net.bouzuya.blog.R;
import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.activity.MainActivity;
import net.bouzuya.blog.entity.Entry;
import net.bouzuya.blog.entity.EntryList;
import net.bouzuya.blog.entity.Optional;
import net.bouzuya.blog.entity.Result;

import javax.inject.Inject;

public class MainService extends IntentService {
    public static final String ACTION_FETCH_ENTRY_LIST =
            "net.bouzuya.blog.action.FETCH_ENTRY_LIST";
    @SuppressWarnings("WeakerAccess")
    @Inject
    EntryRepository entryRepository;

    public MainService() {
        super("MainService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((BlogApplication) getApplication()).getComponent().inject(this);
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
        Result<EntryList> result = entryRepository.getAll();
        if (!result.isOk()) return Optional.empty();
        EntryList entryList = result.getValue();
        return entryList.getLatestEntry();
    }
}
