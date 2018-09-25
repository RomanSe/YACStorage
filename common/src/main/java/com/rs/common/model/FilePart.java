package com.rs.common.model;

import java.io.Serializable;

public class FilePart implements Serializable {
    private int partNumber;
    //TODO hash
    private int size;
    private byte[] bytes;

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
