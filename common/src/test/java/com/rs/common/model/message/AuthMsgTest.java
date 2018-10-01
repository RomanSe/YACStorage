package com.rs.common.model.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class AuthMsgTest {
    AuthMsg authMsg;

    @BeforeEach
    void before() {
        try {
            authMsg =  new AuthMsg("login1", "hello world!");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getLogin() {
        assertEquals("login1", authMsg.getLogin());
    }

    @Test
    void setLogin() {
        authMsg.setLogin("login2");
        assertEquals("login2", authMsg.getLogin());
    }

    @Test
    void getPasswordHash() {
        assertEquals( "dQnlvaDHYtK6x/kNdYtbImP6Acy8VCq1498WO+CObKk=", authMsg.getPasswordHash());
    }
}