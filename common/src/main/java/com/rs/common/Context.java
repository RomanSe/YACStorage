package com.rs.common;

public class Context {
    private String login;
    private String rootPath;
    private TempFile tempFile;
    private byte[] buffer;

    public byte[] getBuffer() {
        if (buffer == null)
            buffer = new byte[DefaultConfig.FILE_CHANK_SIZE];
        return buffer;
    }


    public TempFile getTempFile() {
        return tempFile;
    }

    public void setTempFile(TempFile tempFile) {
        this.tempFile = tempFile;
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
