/*
 * MCDocker, an open source Minecraft launcher.
 * Copyright (C) 2021 MCDocker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.mcdocker.launcher.utils.http;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.mcdocker.launcher.utils.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

            if(method != Method.GET && body != null) {
                connection.setDoOutput(true);
                OutputStream outStream = connection.getOutputStream();
                OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
                outStreamWriter.write(body);
                outStreamWriter.flush();
                outStreamWriter.close();
                outStream.close();
            }
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException ex) {
                if(url.contains("authserver.mojang.com") || url.contains("api.minecraftservices.com"))
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                else {
                    sendConnectError(ex);
                    return null;
                }
            }
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
            String res = response.toString();
            if(pretty) res = gson.newBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(res));
            return res;
        } catch (IOException e) {
            sendConnectError(e);

            return null;
        }
    }
    private void sendConnectError(IOException e) {
        Logger.err("Could not send request to `" + url + "`. Message: " + e.getMessage());
    }

}
