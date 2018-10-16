package com.rs.common;

import com.rs.common.DefaultConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Context {
    private String login;
    private String rootPath;
    private FileProcessingInfo fileProcessingInfo;

    public FileProcessingInfo getFileProcessingInfo() {
        return fileProcessingInfo;
    }

    public void setFileProcessingInfo(FileProcessingInfo fileProcessingInfo) {
        this.fileProcessingInfo = fileProcessingInfo;
    }



    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRootPath() {
        return rootPath;
    }

    public boolean isAuthorized() {
        return login != null;
    }
}
