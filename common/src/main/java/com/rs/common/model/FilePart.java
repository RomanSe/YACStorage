package com.rs.common.model;


import com.rs.common.DefaultConfig;

import java.io.RandomAccessFile;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FilePart implements Serializable {

    private static final long serialVersionUID = 1623427494698957562L;
    //TODO hash

    private byte[] bytes;
    private long startPos;
    private String hash;
    private int length;
    public int getLength() {
        return length;
    }



    public void setLength(int length) {
        this.length = length;
    }



    public FilePart() {
        bytes = new byte[DefaultConfig.FILE_CHANK_SIZE];
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setDigest() {
        this.hash = getDigest();
    }

    public boolean damaged() {
        if (hash == null || hash.equals(getDigest()))
            return false;
        else
            return true;
    }

    private String getDigest() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(getBytes());
            return md.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public void fill(RandomAccessFile file) {

    }

}
