package com.rs.common.messages;

import com.rs.common.model.FileDescr;

public class DeleteFileCommand extends FileCommand {
    private static final long serialVersionUID = 4060702193752704163L;

    public DeleteFileCommand(FileDescr fileDescr) {
        this.fileDescr = fileDescr;
    }
}
