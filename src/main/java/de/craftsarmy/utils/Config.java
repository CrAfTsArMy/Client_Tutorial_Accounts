package de.craftsarmy.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Config {

    private final ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>();

    public void load() {
        try {
            File f = new File("./config/config.json");
            if (!f.getParentFile().exists())
                f.getParentFile().mkdirs();
            boolean joined = f.createNewFile();
            if(!joined) {
                JsonObject data = JsonParser.parseReader(new BufferedReader(new InputStreamReader(new FileInputStream(f)))).getAsJsonObject();
                for (JsonElement e : data.getAsJsonArray("keys"))
                    if (e.isJsonObject()) {
                        JsonObject temp = e.getAsJsonObject();
                        tokens.put(temp.get("user").getAsString(), temp.get("token").getAsString());
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            File f = new File("./config/config.json");
            if (!f.getParentFile().exists())
                f.getParentFile().mkdirs();
            f.createNewFile();
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(f)));
            writer.beginObject().name("keys").beginArray();
            for (String user : tokens.keySet()) {
                writer.beginObject()
                        .name("user").value(user)
                        .name("token").value(tokens.get(user))
                        .endObject();
            }
            writer.endArray().endObject().flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String user, List<Character> key, String token) {
        tokens.put(user, key(key).encrypt(token));
    }

    public String token(String user, List<Character> key) {
        return key(key).decrypt(tokens.get(user));
    }

    private Encryption key(List<Character> key) {
        return new Encryption(key);
    }

    private static Config instance;

    public static Config instance() {
        if (instance == null)
            instance = new Config();
        return instance;
    }

}
