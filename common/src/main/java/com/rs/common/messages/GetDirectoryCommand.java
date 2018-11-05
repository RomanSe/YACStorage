package com.rs.common.messages;


import com.rs.common.model.FileDescr;

public class GetDirectoryCommand extends FileCommand{
    private static final long serialVersionUID = 5439113326478580430L;

    public GetDirectoryCommand(FileDescr fileDescr) {
        this.fileDescr = fileDescr;
    }
}
