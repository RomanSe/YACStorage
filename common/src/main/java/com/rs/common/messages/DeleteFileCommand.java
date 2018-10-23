package com.rs.common.messages;

import com.rs.common.model.FileDescriptor;

public class DeleteFileCommand extends FileCommand {
    private static final long serialVersionUID = 4060702193752704163L;

    public DeleteFileCommand(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }
}
