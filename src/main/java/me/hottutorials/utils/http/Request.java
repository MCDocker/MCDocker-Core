package me.hottutorials.utils.http;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import me.hottutorials.fx.components.Notification;
import me.hottutorials.utils.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Request {

    private final String url;
    private final Method method;
    private final List<Header> headers;
    private final String body;

    private final Gson gson = new Gson();

    public Request(String url, Method method, List<Header> headers, String body) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    public String getURL() { return this.url; }
    public Method getMethod() { return this.method; }
    public List<Header> getHeaders() { return this.headers; }
    public String getBody() { return this.body; }
    public String send() {
        return send(false);
    }

    public String send(boolean pretty) {
        try {
            String newUrl = url.replaceAll("\"", "%22");
            HttpURLConnection connection = (HttpURLConnection) new URL(newUrl).openConnection();
            connection.setRequestMethod(method.name().toUpperCase());
            for(Header header : headers)
                connection.setRequestProperty(header.getKey(), header.getValue());

            if(method == Method.POST && body != null) {
                connection.setDoOutput(true);
                OutputStream outStream = connection.getOutputStream();
                OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
                outStreamWriter.write(body);
                outStreamWriter.flush();
                outStreamWriter.close();
                outStream.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
            String res = response.toString();
            if(pretty) res = gson.newBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(res));
            return res;
        } catch (IOException e) {
            Logger.err("Could not send request to `" + url + "`. Message: " + e.getMessage());

            new Notification("Failed to connect", "Failed to connect to " + url + ". Please check your internet connection and try again", Notification.NotificationType.ERROR);

            return null;
        }
    }

}
