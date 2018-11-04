package com.rs.common;

import com.rs.common.model.FileDescriptor;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileUtilities {
    public static Path getFilePath(Path rootPath, String path) {
        return getFilePath(rootPath.toString(), path);
    }

    public static Path getFilePath(String rootPath, String path) {
        Path p = Paths.get(rootPath, path).normalize();
        return p.toAbsolutePath();
    }

    public static ArrayList<FileDescriptor> getRelativeDirectoryList(FileDescriptor dirDescriptor) throws IOException {
        ArrayList<FileDescriptor> filesList = new ArrayList<>();
        filesList.add(dirDescriptor.getUpDirectory());
        DirectoryStream<Path> stream = Files.newDirectoryStream(dirDescriptor.getAbsolutePath());
        for (Path path : stream) {
            FileDescriptor fileDescriptor = new FileDescriptor(dirDescriptor.getRoot());
            fileDescriptor.setAbsolutePath(path);
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



    public static String changeFileName(String path, String name) {
        Path parent = Paths.get(path).getParent();
        if (parent != null)
            return parent.resolve(name).toString();
        else
            return name;
    }
}
