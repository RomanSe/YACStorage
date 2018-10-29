package com.rs.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TempFile extends RandomAccessFile {
    private Path tempFilePath;
    private Path targetPath;

    public TempFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public TempFile(File file, String mode) throws FileNotFoundException {
        super(file, mode);
    }

    public static TempFile getInstance(Path rootPath, String path, String name) throws IOException {
        Path tempFilePath = FileUtilities.getFilePath(rootPath, path, name + DefaultConfig.PART_FILE_EXT);
        if (!Files.exists(tempFilePath)) {
            System.out.println("create " + tempFilePath.getParent());
            try {
                Files.createDirectory(tempFilePath.getParent());
            } catch (FileAlreadyExistsException e) {
                System.out.println("Директория "+ tempFilePath.getParent() +" уже существует");
            }
        }
        Files.deleteIfExists(tempFilePath);
        TempFile t = new TempFile(tempFilePath.toFile(), "rw");
        t.targetPath = FileUtilities.getFilePath(rootPath, path, name);
        t.tempFilePath = tempFilePath;
        return t;
    }


    public Path getTempFilePath() {
        return tempFilePath;
    }

    public Path getTargetPath() {
        return targetPath;
    }

    public void moveToTarget() throws IOException {
        close();
        if (Files.exists(targetPath))
            Files.delete(targetPath);
        Files.move(tempFilePath, targetPath);
    }

}
