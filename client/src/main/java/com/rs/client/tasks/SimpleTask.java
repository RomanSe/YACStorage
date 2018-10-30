package com.rs.client.tasks;

import com.rs.client.network.NetworkClient;
import com.rs.common.messages.Command;
import com.rs.common.messages.Response;
import javafx.concurrent.Task;
import org.apache.log4j.Logger;

import static com.rs.common.messages.ResponseCode.OK;

//Login and register
public class SimpleTask extends Task<Response> {
    static Logger logger = Logger.getRootLogger();

    private final Command command;

    public SimpleTask(Command command)  {
        this.command = command;
    }

    @Override
    protected Response call() throws Exception {
        NetworkClient networkClient = NetworkClient.getInstance();
        networkClient.invoke(command);
        Response response = networkClient.getResponse();
        if (response.getResponseCode() != OK) {
            logger.info(response.getResponseCode().getMessage());
            throw new Exception(response.getResponseCode().getMessage());
        }
        return response;
    }
}
