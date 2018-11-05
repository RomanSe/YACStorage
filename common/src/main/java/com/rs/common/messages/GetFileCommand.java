package com.rs.common.messages;

import com.rs.common.model.FileDescr;

public class GetFileCommand extends FileCommand {

    private static final long serialVersionUID = 7797172053116729390L;

    private long startPos;
    private int length;

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public GetFileCommand(FileDescr fileDescr) {
        this.fileDescr = fileDescr;
    }

}
