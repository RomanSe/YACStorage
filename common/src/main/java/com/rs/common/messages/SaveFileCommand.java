package com.rs.common.messages;

import com.rs.common.Context;
import com.rs.common.DefaultConfig;
import com.rs.common.TempFile;
import com.rs.common.FileUtilities;
import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.rs.common.messages.ResponseCode.*;

public class SaveFileCommand extends Command {
    private static final long serialVersionUID = -114964002600995666L;
    private FileDescriptor fileDescriptor;
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

    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    //////////////////////////////////////////////  TODO реорганизовать код, чтобы вынести это в серверную часть

    public Response process(Context context) {
        Response response = Response.getInstance();
        if (!context.isAuthorized()) {
            response.setResponseCode(ACCESS_DENIED);
            return response;
        }
        try {
            TempFile tempFile = context.getTempFile();
            Path targetFilePath = FileUtilities.getFilePath(context.getRootPath(), fileDescriptor.getPath(), fileDescriptor.getName());
            if (tempFile != null && !tempFile.getTargetPath().equals(targetFilePath)) {
                System.out.println("Remove " + tempFile.getTempFilePath());
                tempFile.close();
                Files.deleteIfExists(tempFile.getTempFilePath()); //удаляем предыдущий временный
                tempFile = null;
            }
            if (tempFile == null ) {
                tempFile = TempFile.getInstance(context.getRootPath(), fileDescriptor.getPath(), fileDescriptor.getName());
                context.setTempFile(tempFile);
            }
//            filePart.getBytes()[0] = 0;//поломаем
            if (filePart.damaged()) {
                response.setResponseCode(FILE_CORRUPTED);
                return response;
            } else {
                tempFile.seek(filePart.getStartPos());
                tempFile.write(filePart.getBytes(), 0, filePart.getLength());
                if (tempFile.getFilePointer() == fileDescriptor.getSize()) {  //файл записан
                    tempFile.moveToTarget();
                    context.setTempFile(null);
                }
                response.setResponseCode(OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.setResponseCode(CANNOT_SAVE_FILE);
            response.setErrorDescription(e.getLocalizedMessage());
            return response;
        }
        return response;
    }
}
