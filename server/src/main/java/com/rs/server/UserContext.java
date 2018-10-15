package com.rs.server;

import com.rs.common.DefaultConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UserContext {
    private String login;
    private boolean isAuthorized;
    private String rootPath;

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
        this.rootPath = Paths.get(DefaultConfig.SERVER_ROOT_PATH, login).toString();
    }

    public String getRootPath() {
        return rootPath;
    }
}
