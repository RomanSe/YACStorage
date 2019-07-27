package com.rs.common.messages;

import com.rs.common.model.FileDescr;


public class MoveCommand extends FileCommand {
    private static final long serialVersionUID = 2745630059078720834L;
    private FileDescr newFileDescr;

    public MoveCommand(FileDescr fileDescr, FileDescr newFileDescr) {
        this.fileDescr = fileDescr;
        this.newFileDescr = newFileDescr;
    }

    public FileDescr getNewFileDescr() {
        return newFileDescr;
    }

    public void setNewFileDescr(FileDescr newFileDescr) {
        this.newFileDescr = newFileDescr;
    }

}
