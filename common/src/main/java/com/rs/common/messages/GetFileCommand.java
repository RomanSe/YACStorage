package com.rs.common.messages;

import com.rs.common.Context;
import com.rs.common.FileUtilities;
import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.rs.common.messages.ResponseCode.*;

public class GetFileCommand extends Command {

    private static final long serialVersionUID = 7797172053116729390L;

    private FileDescriptor fileDescriptor;
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

    public GetFileCommand(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    //////////////////////////////////////////////////////////////////////
    @Override
    public Response process(Context context) {
        Response response = Response.getInstance();
        if (!context.isAuthorized()) {
            response.setResponseCode(ACCESS_DENIED);
            return response;
        }
        System.out.println(fileDescriptor.getName() + " " + startPos);
        try {
            Path targetFilePath = FileUtilities.getFilePath(context.getRootPath(), fileDescriptor.getPath(), fileDescriptor.getName());
            if (!Files.exists(targetFilePath)) {
                response.setResponseCode(FILE_NOT_FOUND);
                return response;
            }
            FilePart filePart = new FilePart();
            RandomAccessFile file = new RandomAccessFile(targetFilePath.toFile(), "r");
            file.seek(startPos);
            int count = file.read(filePart.getBytes(), 0, length);
            if (count < length) {
                response.setResponseCode(ResponseCode.COMPLETE);
            } else {
                response.setResponseCode(OK);
            }
            filePart.setLength(count);
            filePart.setStartPos(startPos);
            filePart.setDigest();
            response.setFilePart(filePart);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            response.setResponseCode(CANNOT_SAVE_FILE);
            response.setErrorDescription(e.getLocalizedMessage());
        }
        return response;
    }
}
