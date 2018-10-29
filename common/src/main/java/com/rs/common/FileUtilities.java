package com.rs.common;

import com.rs.common.model.FileDescriptor;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileUtilities {
    public static Path getFilePath(Path rootPath, String path, String name) {
        Path p = Paths.get(rootPath.toString(), path).normalize(); //TODO сделать проверку на безопасность пути
        return Paths.get(p.toString(), name).toAbsolutePath();
    }

    public static Path getFilePath(String rootPath, String path, String name) {
        Path p = Paths.get(rootPath, path).normalize(); //TODO сделать проверку на безопасность пути
        return Paths.get(p.toString(), name).toAbsolutePath();
    }

    public static ArrayList<FileDescriptor> getRelativeDirectoryList(Path directoryPath, Path rootPath) throws IOException {
        ArrayList<FileDescriptor> filesList = new ArrayList<>();
        DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath);
        for (Path path : stream) {
            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setName(path.getFileName().toString());
            fileDescriptor.setPath(rootPath.relativize(path.getParent()).toString());
            if (Files.isDirectory(path)) {
                fileDescriptor.setDirectory(true);
            } else {
                fileDescriptor.setDirectory(false);
                fileDescriptor.setSize(Files.size(path));
            }
            filesList.add(fileDescriptor);
        }
        return filesList;
    }
}
