package com.rs.common.messages;

import com.rs.common.model.FileDescr;

public abstract class FileCommand extends Command{

    private static final long serialVersionUID = 2892063668890502560L;
    protected FileDescr fileDescr;

    public FileDescr getFileDescr() {
        return fileDescr;
    }

    public void setFileDescr(FileDescr fileDescr) {
        this.fileDescr = fileDescr;
    }
}
