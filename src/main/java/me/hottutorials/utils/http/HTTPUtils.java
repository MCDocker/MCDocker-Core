package me.hottutorials.utils.http;

import me.hottutorials.utils.Logger;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HTTPUtils {

    public static Map<String, String> getQuery(String url) {
        try {
            String query = new URL(url).getQuery();
            String[] params = query.split("&");
            Map<String, String> map = new HashMap<String, String>();
            for (String param : params)
            {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }
            return map;
        } catch (MalformedURLException e) {
            Logger.err("URL `" + url + "` is malformed. Message: " + e.getMessage());
            return null;
        }
    }

    public static void download(String url, String target) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream outputStream = new FileOutputStream(target)) {
            byte[] data = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                outputStream.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            Logger.err(e);
        }

    }

}
