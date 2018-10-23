package com.rs.common.messages;

import com.rs.common.model.FileDescriptor;
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

    public SaveFileCommand(FileDescriptor fileDescriptor, FilePart filePart) {
        this.fileDescriptor = fileDescriptor;
        this.filePart = filePart;
    }

}
