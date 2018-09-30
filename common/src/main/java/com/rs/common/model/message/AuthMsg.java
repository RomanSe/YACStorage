package com.rs.common.model.message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class AuthMsg {
    private String login;
    private String passwordHash;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            this.passwordHash = DatatypeConverter.printBase64Binary(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}

