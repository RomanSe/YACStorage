package com.rs.common.messages;

import com.rs.common.model.FileDescr;
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

    public ArrayList<FileDescr> getFileDescrList() {
        return fileDescrList;
    }

    public void setFileDescrList(ArrayList<FileDescr> fileDescrList) {
        this.fileDescrList = fileDescrList;
    }

    public FilePart getFilePart() {
        return filePart;
    }

    public void setFilePart(FilePart filePart) {
        this.filePart = filePart;
    }

    private ArrayList<FileDescr> fileDescrList;
    private FilePart filePart;

}
