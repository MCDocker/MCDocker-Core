package me.hottutorials.content.modrinth;

import me.hottutorials.utils.Constants;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.RequestBuilder;

public class Modrinth {

    public static String getMods(Filter filter) {
        return RequestBuilder.getBuilder()
                .setURL(Constants.URLs.MODRINTH_API.getURL() + "api/v1/mod" + (filter == null ? "" : "?" + filter.getQuery()))
                .setMethod(Method.GET)
                .send(true);
    }

    public static String getMods() {
        return getMods(null);
    }

    public static String getMod(String id) {
        return RequestBuilder.getBuilder()
                .setURL(Constants.URLs.MODRINTH_API.getURL() + "api/v1/mod/" + id)
                .setMethod(Method.GET)
                .send(true);
    }

}
