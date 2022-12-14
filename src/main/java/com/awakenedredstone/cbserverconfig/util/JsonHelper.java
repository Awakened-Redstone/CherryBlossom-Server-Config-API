package com.awakenedredstone.cbserverconfig.util;

import com.awakenedredstone.cbserverconfig.CBServerConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonHelper {

    @Nullable
    public static JsonElement parseJsonFile(File file) {
        if (file != null && file.exists() && file.isFile() && file.canRead()) {
            String fileName = file.getAbsolutePath();

            try {
                FileReader reader = new FileReader(file);

                JsonElement element = JsonParser.parseReader(reader);
                reader.close();

                return element;
            } catch (Exception e) {
                CBServerConfig.LOGGER.error(String.format("Failed to parse the JSON file '%s'%n", file.getAbsolutePath()), e);
            }
        }

        return null;
    }

    public static boolean writeJsonToFile(JsonObject root, File file) {
        FileWriter writer = null;

        try {
            writer = new FileWriter(file);
            writer.write(Constants.GSON.toJson(root));
            writer.close();

            return true;
        } catch (IOException e) {
            CBServerConfig.LOGGER.error(String.format("Failed to write JSON data to file '%s'%n", file.getAbsolutePath()), e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                CBServerConfig.LOGGER.error("Failed to close JSON file", e);
            }
        }

        return false;
    }

    @Nullable
    public static JsonObject getNestedObject(@NotNull JsonObject parent, String key, boolean create) {
        if (!parent.has(key) || !parent.get(key).isJsonObject()) {
            if (!create) {
                return null;
            }

            JsonObject obj = new JsonObject();
            parent.add(key, obj);
            return obj;
        } else {
            return parent.get(key).getAsJsonObject();
        }
    }
}
