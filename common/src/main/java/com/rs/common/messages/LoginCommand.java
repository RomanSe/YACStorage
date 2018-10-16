package com.rs.common.messages;

import com.rs.common.Context;
import com.rs.common.DefaultConfig;

import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

import static com.rs.common.messages.ResponseCode.ALREADY_LOGGED_IN;
import static com.rs.common.messages.ResponseCode.INVALID_LOGIN;
import static com.rs.common.messages.ResponseCode.OK;

public class LoginCommand extends Command {
    private static final long serialVersionUID = 6277641625152064169L;
    private String login;
    private String passwordHash;

    public LoginCommand(String login, String password) throws NoSuchAlgorithmException {
        this.login = login;
        this.passwordHash = generateHash(password);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    private String generateHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printBase64Binary(digest);
    }

    public Response process(Context context) {
        Response response = Response.getInstance();
        ResponseCode responseCode;
        if (context.getLogin() != null) {
            responseCode = ALREADY_LOGGED_IN;
        } else {
            //TODO добавить проверку пароля и логина
            if (login.equals("user")) {
                context.setLogin(login);
                context.setRootPath(Paths.get(DefaultConfig.SERVER_ROOT_PATH, login).toString());
                responseCode = OK;
            } else {
                responseCode = INVALID_LOGIN;
            }
        }
        response.setResponseCode(responseCode);
        return response;
    }
}

