package com.rs.common.messages;

import com.rs.common.model.FileDescr;
import com.rs.common.model.FilePart;

public class SaveFileCommand extends FileCommand {
    private static final long serialVersionUID = -114964002600995666L;
    private FilePart filePart;

    public FilePart getFilePart() {
        return filePart;
    }

    public void setFilePart(FilePart filePart) {
        this.filePart = filePart;
    }

    public SaveFileCommand(FileDescr fileDescr, FilePart filePart) {
        this.fileDescr = fileDescr;
        this.filePart = filePart;
    }

}
