package com.rs.client.tasks;

import com.rs.client.network.NetworkClient;
import com.rs.common.messages.GetDirectoryCommand;
import com.rs.common.messages.Response;
import com.rs.common.model.FileDescr;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import static com.rs.common.messages.ResponseCode.OK;

public class GetRemoteDirectoryTask extends Task<Response> {
    private FileDescr fileDescr;
    private ObservableList<FileDescr> list;

    public GetRemoteDirectoryTask(FileDescr fileDescr, ObservableList<FileDescr> list) {
        this.fileDescr = fileDescr;
        this.list = list;
    }

    @Override
    protected Response call() throws Exception {
        NetworkClient networkClient = NetworkClient.getInstance();
        networkClient.invoke(new GetDirectoryCommand(fileDescr));
        Response response = networkClient.getResponse();
        if (response.getResponseCode() != OK) {
            throw new Exception(response.getResponseCode().getMessage() + "\n" + response.getErrorDescription());
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                list.setAll(response.getFileDescrList());
            }
        });
        return response;
    }
}
