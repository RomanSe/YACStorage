package com.rs.client.tasks;

import com.rs.client.network.NetworkClient;
import com.rs.common.messages.GetDirectoryCommand;
import com.rs.common.messages.Response;
import com.rs.common.model.FileDescriptor;
import javafx.application.Platform;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import static com.rs.common.messages.ResponseCode.OK;

public class GetDirectoryTask extends Task<Response> {
    private FileDescriptor fileDescriptor;
    private ObservableList<FileDescriptor> list;

    public GetDirectoryTask(FileDescriptor fileDescriptor, ObservableList<FileDescriptor> list) {
        this.fileDescriptor = fileDescriptor;
        this.list = list;
    }

    @Override
    protected  Response call() throws Exception {
        NetworkClient networkClient = NetworkClient.getInstance();
        networkClient.invoke(new GetDirectoryCommand(fileDescriptor));
        Response response = networkClient.getResponse();
        if (response.getResponseCode() != OK) {
            throw new Exception(response.getResponseCode().getMessage() + "\n" + response.getErrorDescription());
        } else {
            //для отладки
            for (FileDescriptor fDescriptor: response.getFileDescriptorList()) {
                System.out.println(fDescriptor);
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                list.setAll(response.getFileDescriptorList());
            }
        });
        return response;
    }
}
