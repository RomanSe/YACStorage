package com.rs.client;

import com.rs.client.network.NetworkClient;
import com.rs.common.DefaultConfig;
import com.rs.common.FileUtilities;
import com.rs.common.TempFile;
import com.rs.common.messages.GetFileCommand;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.Response;
import com.rs.common.messages.SaveFileCommand;
import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;

import java.io.RandomAccessFile;
import java.nio.file.Path;

import static com.rs.common.messages.ResponseCode.*;

//TODO вынести в отдельный поток
public class Worker extends Thread {
    private static NetworkClient networkClient;

    public static void login(String user, String password) throws Exception {
        if (networkClient == null) {
            networkClient = new NetworkClient(DefaultConfig.HOST, DefaultConfig.PORT);
            networkClient.start();
        }
        networkClient.invoke(new LoginCommand(user, password));
        Response response = networkClient.getResponse();
        if (response.getResponseCode() != OK) {
            throw new Exception(response.getResponseCode().getMessage());
        }
    }

    public static void saveFile(FileDescriptor fileDescriptor) throws Exception {
        if (networkClient == null) {
            throw new Exception("Необходимо залогиниться");
        }
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
                do {
                    networkClient.invoke(saveFileCommand);
                    response = networkClient.getResponse();
                    System.out.printf("%%%.2f\n", position * 100.0 / fileDescriptor.getSize());
                } while (response.getResponseCode() == FILE_CORRUPTED);
                if (response.getResponseCode().isError()) {
                    throw new Exception(response.getResponseCode().getMessage());
                }
            }
        }
    }

    public static void downloadFile(FileDescriptor fileDescriptor) throws Exception {
        if (networkClient == null) {
            throw new Exception("Необходимо залогиниться");
        }
        Response response;
        TempFile tempFile = TempFile.getInstance(DefaultConfig.CLIENT_ROOT_PATH, fileDescriptor.getPath(), fileDescriptor.getName());
        try {
            GetFileCommand getFileCommand = new GetFileCommand(fileDescriptor);
            getFileCommand.setLength(DefaultConfig.FILE_CHANK_SIZE);
            long position = 0;
            while (true) {
                getFileCommand.setStartPos(position);
                do {
                    networkClient.invoke(getFileCommand);
                    response = networkClient.getResponse();
                    //System.out.printf("%%%.2f\n", position * 100.0 / 100);
                } while (response.getFilePart().damaged());
                if (response.getResponseCode().isError()) {
                    throw new Exception(response.getResponseCode().getMessage());
                }
                if (response.getFilePart().getLength() > 0) {
                    tempFile.seek(position);
                    tempFile.write(response.getFilePart().getBytes(), 0, response.getFilePart().getLength());
                }
                position += response.getFilePart().getLength();
                if (response.getResponseCode() == COMPLETE) break;
            }
            tempFile.moveToTarget();
        } finally {
            tempFile.close();
        }
    }

}
