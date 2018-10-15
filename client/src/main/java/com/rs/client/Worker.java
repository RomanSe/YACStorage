package com.rs.client;

import com.rs.client.network.NetworkClient;
import com.rs.common.DefaultConfig;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.Response;
import com.rs.common.messages.SaveFileCommand;
import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;

import java.io.RandomAccessFile;
import java.sql.SQLOutput;
import java.util.Arrays;

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
//            if (fileDescriptor.getSize() > position + DefaultConfig.FILE_CHANK_SIZE) {
//                length = DefaultConfig.FILE_CHANK_SIZE;
//            } else {
//                length = fileDescriptor.getSize() - position;
//            }
                filePart.setStartPos(position);
                filePart.setLength(count);
                filePart.setDigest();
                position += count;
                file.seek(position);
                //System.out.println(count);
                //System.out.println(Arrays.toString(filePart.getBytes()));
                Response response;
                do {
                    networkClient.invoke(saveFileCommand);
                    response = networkClient.getResponse();
                    System.out.printf("%%%.2f\n", position * 100.0 / fileDescriptor.getSize());
                } while (response.getResponseCode() == FILE_CORRUPTED);
                if (response.getResponseCode() != OK) {
                    throw new Exception(response.getResponseCode().getMessage());
                }
            }
        }
    }
}
