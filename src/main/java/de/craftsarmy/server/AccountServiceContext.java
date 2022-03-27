package de.craftsarmy.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.craftsarmy.utils.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceContext implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        parse(exchange, reader.readLine());
    }

    private void parse(HttpExchange exchange, String request) throws IOException {
        try {
            StringWriter response = new StringWriter();
            JsonWriter writer = new JsonWriter(response);

            String user = exchange.getRequestURI().toString().replace("/account/", "");
            JsonObject data = JsonParser.parseString(request).getAsJsonObject();

            if (exchange.getRequestMethod().trim().toLowerCase().contains("post") && data.get("key").isJsonArray()) {
                if (!user.trim().equalsIgnoreCase("") && !user.trim().startsWith("login")) {
                    writer.beginObject();
                    writer.name("code").value("200");
                    writer.name("message").value("OK");
                    List<Character> key = new ArrayList<>();
                    for (JsonElement e : data.getAsJsonArray("key"))
                        if (e.isJsonPrimitive())
                            key.add(e.getAsCharacter());
                    writer.name("repsone").beginObject()
                            .name("user").value(user)
                            .name("token").value(Config.instance().token(user, key));
                    writer.endObject().flush();
                } else if (user.trim().startsWith("login")) {
                    user = user.replace("login/", "");
                    if (!user.trim().equalsIgnoreCase("")) {
                        writer.beginObject();
                        writer.name("code").value("200");
                        writer.name("message").value("OK");
                        List<Character> key = new ArrayList<>();
                        for (JsonElement e : data.getAsJsonArray("key"))
                            if (e.isJsonPrimitive())
                                key.add(e.getAsCharacter());
                        Config.instance().put(user, key, data.get("token").getAsString());
                        writer.name("repsone").beginObject()
                                .name("user").value(user)
                                .name("token").value(Config.instance().token(user, key));
                        writer.endObject().flush();
                    } else {
                        writer.beginObject();
                        writer.name("code").value("400");
                        writer.name("message").value("BAD REQUEST");
                        writer.endObject().flush();
                    }
                } else {
                    writer.beginObject();
                    writer.name("code").value("400");
                    writer.name("message").value("BAD REQUEST");
                    writer.endObject().flush();
                }
            } else {
                writer.beginObject();
                writer.name("code").value("400");
                writer.name("message").value("BAD REQUEST");
                writer.endObject().flush();
            }

            exchange.sendResponseHeaders(200, response.toString().length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            return;
        } catch (Exception ignored) {
        }
        StringWriter response = new StringWriter();
        JsonWriter writer = new JsonWriter(response);
        writer.beginObject();
        writer.name("code").value("400");
        writer.name("message").value("BAD REQUEST");
        writer.endObject().flush();
        exchange.sendResponseHeaders(200, response.toString().length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
