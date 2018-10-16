package com.rs.common;

import java.io.RandomAccessFile;
import java.nio.file.Path;

public class FileProcessingInfo {
    private RandomAccessFile tempFile;
    private Path tempFilePath;
    private Path targetPath;

    public void setTempFile(RandomAccessFile tempFile) {
        this.tempFile = tempFile;
    }

    public Path getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(Path tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public void setTargetPath(Path targetPath) {
        this.targetPath = targetPath;
    }

    public Path getTargetPath() {
        return targetPath;
    }

    public RandomAccessFile getTempFile() {
        return tempFile;
    }
}
