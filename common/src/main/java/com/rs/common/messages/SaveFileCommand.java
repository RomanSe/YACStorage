package com.rs.common.messages;

import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;

public class SaveFileCommand extends Command{
    private static final long serialVersionUID = -114964002600995666L;
    private FileDescriptor fileDescriptor;

    public FilePart getFilePart() {
        return filePart;
    }

    public void setFilePart(FilePart filePart) {
        this.filePart = filePart;
    }

    private FilePart filePart;

    public SaveFileCommand(FileDescriptor fileDescriptor, FilePart filePart) {
        this.fileDescriptor = fileDescriptor;
        this.filePart = filePart;
    }

    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

}
