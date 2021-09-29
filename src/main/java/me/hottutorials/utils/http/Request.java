package me.hottutorials.utils.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.hottutorials.utils.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Request {

    private final String url;
    private final Method method;
    private final List<Header> headers;
    private final String body;

    private final Gson gson = new Gson();

    public Request(String url, Method method) {
        this.url = url;
        this.method = method;
        this.headers = new ArrayList<>();
        this.body = null;
    }
    public Request(String url, Method method, String body) {
        this.url = url;
        this.method = method;
        this.headers = new ArrayList<>();
        this.body = body;
    }
    public Request(String url, Method method, List<Header> headers) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = null;
    }
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
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method.name().toUpperCase());
            for(Header header : headers)
                connection.setRequestProperty(header.getKey(), header.getValue());

            if(method == Method.POST) {
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
            e.printStackTrace();
            return null;
        }
    }

}
