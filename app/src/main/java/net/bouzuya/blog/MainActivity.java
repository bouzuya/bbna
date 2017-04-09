package net.bouzuya.blog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Entry> entryList = newEntries();
    }

    @NonNull
    private List<Entry> newEntries() {
        List<Entry> entryList = new ArrayList<>();
        entryList.add(new Entry("2017-01-01", "entry 1"));
        entryList.add(new Entry("2017-01-02", "entry 2"));
        entryList.add(new Entry("2017-01-03", "entry 3"));
        return entryList;
    }
}
