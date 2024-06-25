package de.flunar.citybuildsystem.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhookSender {
    private final String webhookUrl;

    public DiscordWebhookSender(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void sendMessage(String message) {
        try {
            JsonObject json = new JsonObject();
            json.add("content", new JsonPrimitive(message));

            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.toString().getBytes());
                os.flush();
            }

            connection.getResponseCode(); // Trigger the request
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
