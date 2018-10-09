package com.rs.server;

import com.rs.common.DefaultConfig;
import com.rs.common.messages.ResponseCode;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.Response;
import com.rs.common.messages.SaveFileCommand;
import com.rs.common.model.FileDescr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandProcessor {

    private UserContext userContext = new UserContext();

    //LOGIN
    public Response process(LoginCommand command) {
        Response response = new Response();
        ResponseCode responseCode = ResponseCode.OK;
        String login = userContext.getLogin();
        if (login != null) {
            responseCode = ResponseCode.ALREADY_LOGGED_IN;
        } else {
            //TODO добавить проверку пароля и логина
            if (command.getLogin().equals("user")) {
                userContext.setLogin(command.getLogin());
                userContext.setAuthorized(true);
            } else {
                responseCode = ResponseCode.INVALID_LOGIN;
            }
        }
        response.setResponseCode(responseCode);
        return response;
    }

    //SAVE FILE
    public Response process(SaveFileCommand command) {
        Response response = new Response();
        if (!userContext.isAuthorized()) {
            response.setResponseCode(ResponseCode.ACCESS_DENIED);
            return response;
        }
        ResponseCode responseCode = ResponseCode.OK;
        FileDescr file = command.getFileDescr();
        UserContext.FileTransferState transferState = userContext.getFileTransferState();
        try {
            Path p = Paths.get(userContext.getRootPath(),file.getPath()).toRealPath().toAbsolutePath();
            transferState.absolutePath = Paths.get(p.toString(),file.getName()); //TODO сделать проверку пути
            transferState.size = file.getSize();
            transferState.tempPath = transferState.absolutePath + DefaultConfig.PART_FILE_EXT;
            transferState.lastPos = 0;
            transferState.isTransferring = true;
            //create file
            Files.deleteIfExists(Paths.get(transferState.tempPath));
            Files.createFile(Paths.get(transferState.tempPath));
        } catch (IOException e) {
            e.printStackTrace();
            responseCode = ResponseCode.CANNOT_SAVE_FILE;
            response.setErrorDescription(e.getLocalizedMessage());
        }
        response.setResponseCode(responseCode);
        return response;
    }


}
