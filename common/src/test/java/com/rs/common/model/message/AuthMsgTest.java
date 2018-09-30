package com.rs.common.model.message;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthMsgTest {
    AuthMsg authMsg;

    @BeforeEach
    void before() {
        authMsg =  new AuthMsg();
        authMsg.setLogin("login1");
        authMsg.setPasswordHash("hello world!");
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

    @Test
    void setPasswordHash() {
        authMsg.setPasswordHash("hello");
        assertEquals("LPJNul+wow4m6DsqxbninhsWHlwfp0JecwQzYpOLmCQ=", authMsg.getPasswordHash());
    }
}