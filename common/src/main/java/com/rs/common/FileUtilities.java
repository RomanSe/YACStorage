package com.rs.common;

import com.rs.common.model.FileDescr;

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

    public static ArrayList<FileDescr> getRelativeDirectoryList(FileDescr dirDescriptor) throws IOException {
        ArrayList<FileDescr> filesList = new ArrayList<>();
        filesList.add(dirDescriptor.getUpDirectory());
        DirectoryStream<Path> stream = Files.newDirectoryStream(dirDescriptor.getAbsolutePath());
        for (Path path : stream) {
            FileDescr fileDescr = new FileDescr(dirDescriptor.getRoot());
            fileDescr.setAbsolutePath(path);
            if (Files.isDirectory(path)) {
                fileDescr.setDirectory(true);
            } else {
                fileDescr.setDirectory(false);
                fileDescr.setSize(Files.size(path));
            }
            filesList.add(fileDescr);
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
