package com.rs.common.messages;


import com.rs.common.model.FileDescriptor;

public class GetDirectoryCommand extends FileCommand{
    private static final long serialVersionUID = 5439113326478580430L;

    public GetDirectoryCommand(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }
}
