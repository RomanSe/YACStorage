package com.rs.server;

import com.rs.common.DefaultConfig;
import com.rs.common.FileUtilities;
import com.rs.common.TempFile;
import com.rs.common.messages.*;
import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;
import com.rs.server.db.User;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.rs.common.messages.ResponseCode.*;

public class CommandProcessor {
    private static Logger logger = Logger.getRootLogger();

    //GetFileCommand
    public static Response process(GetFileCommand command, Context context) {
        FileDescriptor fileDescriptor = command.getFileDescriptor();
        long startPos = command.getStartPos();
        int length = command.getLength();

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
            logger.error(e.getLocalizedMessage());
            response.setResponseCode(CANNOT_SAVE_FILE);
            response.setErrorDescription(e.getLocalizedMessage());
        }
        return response;
    }

    //SignInCommand
    public static Response process(SignInCommand command, Context context) {
        Response response = Response.getInstance();
        ResponseCode responseCode;
        String login = command.getLogin();
        try {
            if (!User.exists(login)) {
                User user = User.create(login, command.getPasswordHash(), command.getEmail());
                context.setUser(user);
                context.setRootPath(Paths.get(DefaultConfig.SERVER_ROOT_PATH, login));
                responseCode = OK;
            } else {
                responseCode = ResponseCode.LOGIN_IS_BUSY;
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            responseCode = ERROR;
            response.setErrorDescription(e.getLocalizedMessage());
        }
        response.setResponseCode(responseCode);
        return response;
    }

    //LoginCommand
    public static Response process(LoginCommand command, Context context) {
        Response response = Response.getInstance();
        ResponseCode responseCode;
        String login = command.getLogin();
        String passwordHash = command.getPasswordHash();
        try {
            if (User.authenticated(login, passwordHash)) {
                context.setUser(User.get(login));
                context.setRootPath(Paths.get(DefaultConfig.SERVER_ROOT_PATH, login));
                responseCode = OK;
            } else {
                responseCode = INVALID_LOGIN;
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            responseCode = ERROR;
            response.setErrorDescription(e.getLocalizedMessage());
        }
        response.setResponseCode(responseCode);
        return response;
    }

    //SaveFileCommand
    public static Response process(SaveFileCommand command, Context context) {
        FileDescriptor fileDescriptor = command.getFileDescriptor();
        FilePart filePart = command.getFilePart();
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
            if (tempFile == null) {
                tempFile = TempFile.getInstance(context.getRootPath(), fileDescriptor.getPath(), fileDescriptor.getName());
                context.setTempFile(tempFile);
            }
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
            logger.error(e.getLocalizedMessage());
            response.setResponseCode(CANNOT_SAVE_FILE);
            response.setErrorDescription(e.getLocalizedMessage());
            return response;
        }
        return response;
    }

    //MoveCommand
    public static Response process(MoveCommand command, Context context) {
        FileDescriptor fileDescriptor = command.getFileDescriptor();
        FileDescriptor newFileDescriptor = command.getNewFileDescriptor();
        Response response = Response.getInstance();
        Path filePath = FileUtilities.getFilePath(context.getRootPath(), fileDescriptor.getPath(), fileDescriptor.getName());
        Path newFilePath = FileUtilities.getFilePath(context.getRootPath(), newFileDescriptor.getPath(), newFileDescriptor.getName());
        try {
            Files.move(filePath, newFilePath);
            response.setResponseCode(OK);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            response.setResponseCode(ERROR);
            response.setErrorDescription(e.getLocalizedMessage());
        }
        return response;
    }

    //GetDirectoryCommand
    public static Response process(GetDirectoryCommand command, Context context) {
        FileDescriptor directoryDescriptor = command.getFileDescriptor();
        Response response = Response.getInstance();
        Path directoryPath = FileUtilities.getFilePath(context.getRootPath(), directoryDescriptor.getPath(), directoryDescriptor.getName());
        if (!Files.isDirectory(directoryPath)) {
            response.setResponseCode(ResponseCode.DIRECTORY_NOT_FOUND);
            return response;
        }
        ArrayList<FileDescriptor> filesList;
        try {
            filesList = FileUtilities.getRelativeDirectoryList(directoryPath, context.getRootPath());
        } catch (IOException e) {
            response.setResponseCode(ERROR);
            response.setErrorDescription(e.getLocalizedMessage());
            return response;
        }
        response.setResponseCode(OK);
        response.setFileDescriptorList(filesList);
        return response;
    }

    //DeleteFileCommand
    public static Response process(DeleteFileCommand command, Context context) {
        FileDescriptor fileDescriptor = command.getFileDescriptor();
        Response response = Response.getInstance();
        Path filePath = FileUtilities.getFilePath(context.getRootPath(), fileDescriptor.getPath(), fileDescriptor.getName());
        try {
            Files.delete(filePath);
            response.setResponseCode(OK);
        } catch (IOException e) {
            response.setResponseCode(ERROR);
            response.setErrorDescription(e.getMessage());
        }
        return response;
    }

}
