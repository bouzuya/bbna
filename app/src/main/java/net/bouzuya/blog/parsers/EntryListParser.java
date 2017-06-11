package net.bouzuya.blog.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.bouzuya.blog.models.Entry;
import net.bouzuya.blog.models.EntryId;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntryListParser {
    public List<Entry> parse(String jsonString) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Entry.class, new EntryDeserializer())
                .create();
        try {
            Entry[] entryArray = gson.fromJson(jsonString, Entry[].class);
            List<Entry> entryList = Arrays.asList(entryArray);
            // order by desc
            Collections.sort(entryList, new Comparator<Entry>() {
                @Override
                public int compare(Entry e1, Entry e2) {
                    return e2.getId().compareTo(e1.getId());
                }
            });
            return entryList;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static class EntryDeserializer implements JsonDeserializer<Entry> {
        @Override
        public Entry deserialize(
                JsonElement json,
                Type typeOfT,
                JsonDeserializationContext context
        ) throws JsonParseException {
            try {
                return parseEntryJson(json);
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        private Entry parseEntryJson(JsonElement jsonElement) {
            JsonObject jsonEntry = jsonElement.getAsJsonObject();
            String date = jsonEntry.get("date").getAsString();
            EntryId entryId = EntryId.fromISO8601DateString(date);
            String title = jsonEntry.get("title").getAsString();
            return new Entry(entryId, title);
        }
    }
}
