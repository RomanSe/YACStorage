package com.rs.common.messages;

import com.rs.common.model.FileDescriptor;

public abstract class FileCommand extends Command{

    private static final long serialVersionUID = 2892063668890502560L;
    protected FileDescriptor fileDescriptor;

    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    public void setFileDescriptor(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }
}
