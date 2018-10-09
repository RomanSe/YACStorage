package com.rs.client;

import com.rs.client.network.NetworkClient;
import com.rs.common.DefaultConfig;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.Response;
import com.rs.common.messages.ResponseCode;
import com.rs.common.messages.SaveFileCommand;
import com.rs.common.model.FileDescr;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Worker {
    private static NetworkClient networkClient;

    public static void login(String user, String password) throws Exception {
        if (networkClient == null) {
            networkClient = new NetworkClient(DefaultConfig.HOST, DefaultConfig.PORT);
            networkClient.start();
        }
        networkClient.invoke(new LoginCommand(user, password));
        Response response = networkClient.getResponse();
        if (response.getResponseCode() != ResponseCode.OK) {
            throw new Exception(response.getResponseCode().getMessage());
        }
    }

    public static void saveFile(String path) throws Exception {
        if (networkClient == null) {
            throw new Exception("Необходимо залогиниться");
        }
        FileDescr fileDescr = new FileDescr();
        Path p = Paths.get(path);
        fileDescr.setName("123.txt");
        fileDescr.setPath("");
        fileDescr.setSize(123);
        networkClient.invoke(new SaveFileCommand(fileDescr));
        Response response = networkClient.getResponse();
        if (response.getResponseCode() != ResponseCode.OK) {
            throw new Exception(response.getResponseCode().getMessage());
        }
    }
}
