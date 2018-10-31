package com.rs.common;

import com.rs.common.model.FileDescriptor;
import javafx.scene.Parent;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileUtilities {
    public static Path getFilePath(Path rootPath, String path, String name) {
        Path p = Paths.get(rootPath.toString(), path).normalize();
        return Paths.get(p.toString(), name).toAbsolutePath();
    }

    public static Path getFilePath(String rootPath, String path, String name) {
        Path p = Paths.get(rootPath, path).normalize();
        return Paths.get(p.toString(), name).toAbsolutePath();
    }

    public static ArrayList<FileDescriptor> getRelativeDirectoryList(Path directoryPath, Path rootPath) throws IOException {
        ArrayList<FileDescriptor> filesList = new ArrayList<>();
        DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath);
        for (Path path : stream) {
            FileDescriptor fileDescriptor = new FileDescriptor(rootPath);
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

    public static FileDescriptor getUpDirectory(FileDescriptor currentDir) {
        FileDescriptor upDirectory;
        upDirectory = new FileDescriptor(currentDir.getRoot());
        upDirectory.setRelativePath(currentDir.getRelativePath());
        upDirectory.setName("");
        upDirectory.setDirectory(true);
        return upDirectory;
    }
}
