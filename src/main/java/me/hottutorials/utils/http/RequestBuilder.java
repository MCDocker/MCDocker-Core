package me.hottutorials.utils.http;

import com.google.gson.JsonElement;
import me.hottutorials.utils.Logger;

import java.util.*;

public class RequestBuilder {

    private String url;
    private List<Header> headers = new ArrayList<>();
    private JsonElement body;
    private Method method;

    private final static RequestBuilder INSTANCE = new RequestBuilder();

    public static RequestBuilder getBuilder() { return INSTANCE; }

    public RequestBuilder setMethod(Method method) {
        this.method = method;
        return this;
    }

    public RequestBuilder setURL(String url) {
        this.url = url;
        return this;
    }

    public RequestBuilder addHeader(String key, String value) {
        headers.add(new Header(key, value));
        return this;
    }

    public RequestBuilder addHeaders(List<Header> hdrs) {
        headers.addAll(hdrs);
        return this;
    }

    public RequestBuilder setBody(JsonElement body) {
        this.body = body;
        return this;
    }

    public Request build() {
        return new Request(url, method, headers, body);
    }
    public String send() { return send(false); }
    public String send(boolean pretty) { return new Request(url, method, headers, body).send(pretty); }

}
