package com.rs.common.messages;


import java.security.NoSuchAlgorithmException;

public class SignInCommand extends LoginCommand {
    private static final long serialVersionUID = 6231292321797642979L;
    private String email;

    public String getEmail() {
        return email;
    }

    public SignInCommand(String login, String password, String email) throws NoSuchAlgorithmException {
        super(login, password);
        this.email = email;
    }

}

