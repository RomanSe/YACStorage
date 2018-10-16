package com.rs.common;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtilities {
    public static Path getFilePath(String rootPath, String path, String name) throws IOException {
        Path p = Paths.get(rootPath, path).normalize(); //TODO сделать проверку на безопасность пути
        return Paths.get(p.toString(), name).toAbsolutePath();
    }
}
