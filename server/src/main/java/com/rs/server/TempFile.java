package com.rs.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TempFile extends RandomAccessFile {
    private Path path;

    public Path getPath() {
        return path;
    }

    public TempFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);
        path = Paths.get(name);
    }

    public TempFile(File file, String mode) throws FileNotFoundException {
        super(file, mode);
        path = file.toPath();
    }
}
