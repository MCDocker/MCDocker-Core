package me.hottutorials.utils.http;

import me.hottutorials.utils.Logger;

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

}
