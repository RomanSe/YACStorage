package com.rs.common.model;

import com.rs.common.FileUtilities;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

//все есть файл
public class FileDescriptor implements Serializable  {
    private static final long serialVersionUID = 571710974311506623L;
    private String name = "";
    private String relativePath = "";
    private transient String root = "";
    private long size;
    private boolean isDirectory;
    private String d;

    public FileDescriptor() {

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

    public String getD() {
        return isDirectory?"D":" ";
    }

    public void setD(String d) {
    }

    public Path getAbsolutePath() {
        return FileUtilities.getFilePath(root, relativePath, name);
    }

    public void setAbsolutePath(Path path) {
        setName(path.getFileName().toString());
        if (root.equals(""))
            setRelativePath(path.getParent().toString());
        else
            setRelativePath(Paths.get(root).relativize(path.getParent()).toString());
    }

    public FileDescriptor(Path root) {
        setRoot(root);
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDescriptor that = (FileDescriptor) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(relativePath, that.relativePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, relativePath);
    }

    @Override
    public String toString() {
        return "FileDescriptor{" +
                "name='" + name + '\'' +
                ", relativePath='" + relativePath + '\'' +
                ", size=" + size +
                ", isDirectory=" + isDirectory +
                '}';
    }
}
