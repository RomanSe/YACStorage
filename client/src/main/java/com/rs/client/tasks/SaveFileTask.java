package com.rs.client.tasks;

import com.rs.client.network.NetworkClient;
import com.rs.common.DefaultConfig;
import com.rs.common.messages.Response;
import com.rs.common.messages.SaveFileCommand;
import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;
import javafx.concurrent.Task;

import java.io.RandomAccessFile;

import static com.rs.common.messages.ResponseCode.FILE_CORRUPTED;

public class SaveFileTask extends Task<Response> {
    private FileDescriptor fileDescriptor;

    public SaveFileTask(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    @Override
    protected Response call() throws Exception {
        NetworkClient networkClient = NetworkClient.getInstance();
        try (RandomAccessFile file = new RandomAccessFile(fileDescriptor.getAbsolutePath().toFile(), "r")) {
            FilePart filePart = new FilePart();
            SaveFileCommand saveFileCommand = new SaveFileCommand(fileDescriptor, filePart);
            int count;
            long position = 0;
            while ((count = file.read(filePart.getBytes())) != -1) {
                filePart.setStartPos(position);
                filePart.setLength(count);
                filePart.setDigest();
                position += count;
                file.seek(position);
                Response response;
                int numTry = 0;
                do {
                    networkClient.invoke(saveFileCommand);
                    response = networkClient.getResponse();
                    updateProgress(position, fileDescriptor.getSize());
                    numTry++;
                } while (response.getResponseCode() == FILE_CORRUPTED || numTry > DefaultConfig.TRY_NUM);
                if (response.getResponseCode().isError()) {
                    throw new Exception(response.getResponseCode().getMessage());
                }
            }
            System.out.println(fileDescriptor + " закачан");
        }
        return null;
    }
}
