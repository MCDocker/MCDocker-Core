package me.hottutorials.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FSUtils {

    public static boolean createDirRecursively(String base, String path) {
        List<String> paths = new LinkedList<>(Arrays.asList(path.split("/")));
        try {
            if(paths.size() == 1) {
                Files.createDirectory(Path.of(base, paths.get(0)));
                return true;
            } else {
                Files.createDirectory(Path.of(base, paths.get(0)));
                base = base + "/" + paths.get(0);
                paths.remove(0);

                StringBuilder builder = new StringBuilder();
                paths.forEach(p -> builder.append(p).append("/"));
                builder.deleteCharAt(builder.toString().length() - 1);

                return createDirRecursively(base, builder.toString());
            }
        } catch (IOException e) {
            base = base + "/" + paths.get(0);
            paths.remove(0);

            StringBuilder builder = new StringBuilder();
            paths.forEach(p -> builder.append(p).append("/"));
            builder.deleteCharAt(builder.toString().length() - 1);

            return createDirRecursively(base, builder.toString());
        }
    }

}
