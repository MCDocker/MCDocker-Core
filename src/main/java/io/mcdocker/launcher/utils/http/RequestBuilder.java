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

import java.util.ArrayList;
import java.util.List;

public class RequestBuilder {

    private String url;
    private List<Header> headers = new ArrayList<>();
    private String body;
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

    public RequestBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public Request build() {
        return new Request(url, method, headers, body);
    }
    public String send() { return send(false); }
    public String send(boolean pretty) { return new Request(url, method, headers, body).send(pretty); }

}
