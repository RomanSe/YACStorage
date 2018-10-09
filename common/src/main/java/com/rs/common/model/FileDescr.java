package com.rs.common.model;

import java.io.Serializable;
import java.nio.file.Paths;

//все есть файл
public class FileDescr implements Serializable {
    private static final long serialVersionUID = 571710974311506623L;
    private String name;
    private String path;
    private long size;
    private boolean isDirectory;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getRelativePath() {return Paths.get(path, name).toString();}
}
