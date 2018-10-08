package com.rs.server;

import com.rs.common.model.ResponseCode;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.ResponseMsg;

public class CommandProcessor {
    public static ResponseMsg process(UserContext userContext, LoginCommand command) {
        ResponseMsg responseMsg = new ResponseMsg();
        ResponseCode responseCode = ResponseCode.OK;
        String login = userContext.getLogin();
        if (login != null) {
            responseCode = ResponseCode.ALREADY_LOGGED_IN;
        } else {
            //TODO добавить проверку пароля и логина
            if (command.getLogin().equals("user")) {
                userContext.setLogin(command.getLogin());
            } else {
                responseCode = ResponseCode.NO_LOGIN;
            }
        }
        responseMsg.setResponseCode(responseCode);
        return responseMsg;
    }
}
