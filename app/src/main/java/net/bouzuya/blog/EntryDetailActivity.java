package net.bouzuya.blog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class EntryDetailActivity extends AppCompatActivity {
    private static final String TAG = EntryDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);
        Log.d(TAG, "onCreate: ");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String html = extras.getString("html");
        WebView webView = (WebView) findViewById(R.id.entry_detail);
        webView.loadData(html, "text/html", "UTF-8");
    }
}
