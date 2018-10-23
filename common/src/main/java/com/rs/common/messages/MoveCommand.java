package com.rs.common.messages;

import com.rs.common.model.FileDescriptor;


public class MoveCommand extends FileCommand {
    private static final long serialVersionUID = 2745630059078720834L;
    private FileDescriptor newFileDescriptor;

    public MoveCommand(FileDescriptor fileDescriptor, FileDescriptor newFileDescriptor) {
        this.fileDescriptor = fileDescriptor;
        this.newFileDescriptor = newFileDescriptor;
    }

    public FileDescriptor getNewFileDescriptor() {
        return newFileDescriptor;
    }

    public void setNewFileDescriptor(FileDescriptor newFileDescriptor) {
        this.newFileDescriptor = newFileDescriptor;
    }

}
