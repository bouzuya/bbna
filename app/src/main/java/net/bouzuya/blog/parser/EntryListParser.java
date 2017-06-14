package net.bouzuya.blog.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.bouzuya.blog.model.Entry;
import net.bouzuya.blog.model.EntryId;
import net.bouzuya.blog.model.EntryList;

import java.lang.reflect.Type;

public class EntryListParser {
    public EntryList parse(String jsonString) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Entry.class, new EntryDeserializer())
                .create();
        try {
            Entry[] entryArray = gson.fromJson(jsonString, Entry[].class);
            return EntryList.fromEntryArray(entryArray);
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
