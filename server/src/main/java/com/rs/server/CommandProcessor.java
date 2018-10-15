package com.rs.server;

import com.rs.common.DefaultConfig;
import com.rs.common.messages.ResponseCode;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.Response;
import com.rs.common.messages.SaveFileCommand;
import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static com.rs.common.messages.ResponseCode.*;

public class CommandProcessor {
    private UserContext userContext;
    private static FileProcessor fileProcessor;

    public CommandProcessor() {
        userContext = new UserContext();
        fileProcessor = new FileProcessor(userContext);
    }

    //LOGIN
    public Response process(LoginCommand command) {
        Response response = new Response();
        ResponseCode responseCode = OK;
        String login = userContext.getLogin();
        if (login != null) {
            responseCode = ALREADY_LOGGED_IN;
        } else {
            //TODO добавить проверку пароля и логина
            if (command.getLogin().equals("user")) {
                userContext.setLogin(command.getLogin());
                userContext.setAuthorized(true);
            } else {
                responseCode = INVALID_LOGIN;
            }
        }
        response.setResponseCode(responseCode);
        return response;
    }

    public Response process(SaveFileCommand command) {
        return fileProcessor.process(command);
    }






}
