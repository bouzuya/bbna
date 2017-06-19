package net.bouzuya.blog.driver.repository.request.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.EntryId;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EntryDetailResponseParserImpl implements EntryDetailResponseParser {
    @Override
    public EntryDetail parse(String responseBody) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(EntryDetail.class, new EntryDetailDeserializer())
                .create();
        try {
            return gson.fromJson(responseBody, EntryDetail.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static class EntryDetailDeserializer implements JsonDeserializer<EntryDetail> {
        @Override
        public EntryDetail deserialize(
                JsonElement json,
                Type typeOfT,
                JsonDeserializationContext context
        ) throws JsonParseException {
            try {
                return parseEntryDetailJson(json);
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }

        private EntryDetail parseEntryDetailJson(JsonElement jsonElement) {
            JsonObject jsonEntry = jsonElement.getAsJsonObject();
            return EntryDetail.newBuilder()
                    .setId(EntryId.fromISO8601DateString(jsonEntry.get("date").getAsString()))
                    .setTitle(jsonEntry.get("title").getAsString())
                    .setData(jsonEntry.get("data").getAsString())
                    .setHtml(jsonEntry.get("html").getAsString())
                    .setMinutes(jsonEntry.get("minutes").getAsInt())
                    .setPubdate(jsonEntry.get("pubdate").getAsString())
                    .setTags(parseTagsJson(jsonEntry.get("tags")))
                    .build();
        }

        private List<String> parseTagsJson(JsonElement jsonElement) {
            JsonArray jsonTags = jsonElement.getAsJsonArray();
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < jsonTags.size(); i++) {
                tags.add(jsonTags.get(i).getAsString());
            }
            return tags;
        }

    }
}
