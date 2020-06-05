package com.oneslide.common.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class FileTool {
    /**
     * delete file recursively with jdk8 api,only can delete directory
     *
     * @param pathStr target directory path deleted
     * @return true if the operation is success
     **/
    public static boolean deleteRecursivelyBetter(String pathStr) {
        Path path = Paths.get(pathStr);
        try {
            Files.walk(path).sorted(Comparator.reverseOrder()).forEach(path1 -> {
                new File(path1.toString()).delete();
            });
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
