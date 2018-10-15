package com.rs.common.messages;

import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    private static final long serialVersionUID = -8700849836502485019L;
    private ResponseCode responseCode;

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    private String errorDescription;

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public ArrayList<FileDescriptor> getFileDescriptorList() {
        return fileDescriptorList;
    }

    public void setFileDescriptorList(ArrayList<FileDescriptor> fileDescriptorList) {
        this.fileDescriptorList = fileDescriptorList;
    }

    public FilePart getFilePart() {
        return filePart;
    }

    public void setFilePart(FilePart filePart) {
        this.filePart = filePart;
    }

    private ArrayList<FileDescriptor> fileDescriptorList;
    private FilePart filePart;

}
