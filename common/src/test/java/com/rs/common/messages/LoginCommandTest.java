package com.rs.common.messages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginCommandTest {
    LoginCommand loginCommand;

    @BeforeEach
    void before() {
        loginCommand = new LoginCommand("login1", "hello world!");
    }

    @Test
    void getLogin() {
        assertEquals("login1", loginCommand.getLogin());
    }

    @Test
    void setLogin() {
        loginCommand.setLogin("login2");
        assertEquals("login2", loginCommand.getLogin());
    }

    @Test
    void getPasswordHash() {
        assertEquals("dQnlvaDHYtK6x/kNdYtbImP6Acy8VCq1498WO+CObKk=", loginCommand.getPasswordHash());
    }
}