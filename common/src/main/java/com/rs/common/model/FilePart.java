package com.rs.common.model;

import java.io.Serializable;

public class FilePart implements Serializable {
    private int partNumber;
    //TODO hash
    private byte[] bytes;

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
