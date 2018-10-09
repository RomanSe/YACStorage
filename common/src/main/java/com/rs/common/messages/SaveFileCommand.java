package com.rs.common.messages;

import com.rs.common.model.FileDescr;

public class SaveFileCommand extends Command{
    private static final long serialVersionUID = -114964002600995666L;
    private FileDescr fileDescr;

    public SaveFileCommand(FileDescr fileDescr) {
        this.fileDescr = fileDescr;
    }

    public FileDescr getFileDescr() {
        return fileDescr;
    }

}
