package com.rs.common.model;

import com.rs.common.FileUtilities;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

//все есть файл
public class FileDescriptor implements Serializable {
    private static final long serialVersionUID = 571710974311506623L;
    private String path = "";
    private transient String root = "";
    private Long size;
    private boolean isDirectory;
    private boolean isUp;

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    private final String UP_SIGN = "...";

    public FileDescriptor() {
        path = "";
        root = "";
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void setRoot(Path root) {
        this.root = root.toString();
    }

    public Path getAbsolutePath() {
        if (root == null || root.equals(""))
            return Paths.get(path);
        else
            return FileUtilities.getFilePath(root, path);
    }

    public void setAbsolutePath(Path path) {
        if (root == null || root.equals("")) {
            setPath(path);
        } else
            setPath(Paths.get(root).relativize(path).toString());
    }

    public FileDescriptor(Path root) {
        setRoot(root);
    }

    public FileDescriptor(String root) {
        setRoot(root);
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getName() {
        if (isUp)
            return UP_SIGN;
        else
            return Paths.get(path).getFileName().toString();
    }

    public FileDescriptor getUpDirectory() {
        FileDescriptor upDirectory;
        upDirectory = new FileDescriptor(getRoot());
        upDirectory.setPath(getParent());
        upDirectory.setDirectory(true);
        upDirectory.setUp(true);
        return upDirectory;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = Paths.get(path).normalize().toString();
    }

    public void setPath(Path path) {
        this.path = path.toString();
    }


    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDescriptor that = (FileDescriptor) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "FileDescriptor{" +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", isDirectory=" + isDirectory +
                '}';
    }

    public String getParent() {
        if (!path.equals("")) {
            Path parent = Paths.get(path).getParent();
            if (parent != null) {
                return parent.toString();
            }
        }
        return "";
    }
}
