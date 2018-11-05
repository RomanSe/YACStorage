package com.rs.client.tasks;

import com.rs.client.network.NetworkClient;
import com.rs.common.DefaultConfig;
import com.rs.common.TempFile;
import com.rs.common.messages.GetFileCommand;
import com.rs.common.messages.Response;
import com.rs.common.model.FileDescr;
import javafx.concurrent.Task;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.rs.common.messages.ResponseCode.COMPLETE;

public class DownloadFileTask extends Task<Response> {
    private FileDescr remoteFile;
    private FileDescr localDir;

    public DownloadFileTask(FileDescr remoteFile, FileDescr localDir) {
        this.remoteFile = remoteFile;
        this.localDir = localDir;
    }

    @Override
    protected Response call() throws Exception {
        NetworkClient networkClient = NetworkClient.getInstance();
        Response response;

        Path localPath = Paths.get(localDir.getPath(), remoteFile.getName());
        TempFile tempFile = TempFile.getInstance(localPath);
        try {
            GetFileCommand getFileCommand = new GetFileCommand(remoteFile);
            getFileCommand.setLength(DefaultConfig.FILE_CHANK_SIZE);
            long position = 0;
            while (true) {
                getFileCommand.setStartPos(position);
                do {
                    networkClient.invoke(getFileCommand);
                    response = networkClient.getResponse();
                } while (response.getFilePart().damaged());
                if (response.getResponseCode().isError()) {
                    throw new Exception(response.getResponseCode().getMessage());
                }
                if (response.getFilePart().getLength() > 0) {
                    tempFile.seek(position);
                    tempFile.write(response.getFilePart().getBytes(), 0, response.getFilePart().getLength());
                }
                updateProgress(position, remoteFile.getSize());
                position += response.getFilePart().getLength();
                if (response.getResponseCode() == COMPLETE) break;
            }
            tempFile.moveToTarget();
        } finally {
            tempFile.close();
        }
        return null;
    }
}
