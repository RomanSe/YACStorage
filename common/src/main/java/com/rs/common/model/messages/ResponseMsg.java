package com.rs.common.model.messages;

import com.rs.common.model.File;
import com.rs.common.model.FilePart;
import com.rs.common.model.ResponseCode;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseMsg implements Serializable {
    private static final long serialVersionUID = -8700849836502485019L;
    private ResponseCode responseCode;

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public ArrayList<File> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<File> fileList) {
        this.fileList = fileList;
    }

    public FilePart getFilePart() {
        return filePart;
    }

    public void setFilePart(FilePart filePart) {
        this.filePart = filePart;
    }

    private ArrayList<File> fileList;
    private FilePart filePart;

}
