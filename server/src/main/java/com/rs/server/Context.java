package com.rs.server;

import com.rs.common.DefaultConfig;
import com.rs.common.TempFile;
import com.rs.server.db.User;

import java.nio.file.Path;

public class Context {
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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


    public Path getRootPath() {
        return rootPath;
    }

    public void setRootPath(Path rootPath) {
        this.rootPath = rootPath;
    }

    public boolean isAuthorized() {
        return user != null;
    }

    private User user;
    private Path rootPath;
    private TempFile tempFile;
    private byte[] buffer;

}
