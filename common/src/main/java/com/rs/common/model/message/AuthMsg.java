package com.rs.common.model.message;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class AuthMsg implements Serializable {
    private String login;
    private String passwordHash;

    public AuthMsg(String login, String password) throws NoSuchAlgorithmException {
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
}

