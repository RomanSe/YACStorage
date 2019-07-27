package com.rs.client.tasks;

import com.rs.client.network.NetworkClient;
import com.rs.common.DefaultConfig;
import com.rs.common.messages.Response;
import com.rs.common.messages.SaveFileCommand;
import com.rs.common.model.FileDescr;
import com.rs.common.model.FilePart;
import javafx.concurrent.Task;

import java.io.RandomAccessFile;
import java.nio.file.Paths;

import static com.rs.common.messages.ResponseCode.FILE_CORRUPTED;

public class SaveFileTask extends Task<Response> {
    private FileDescr localFile;
    private FileDescr remoteDir;

    public SaveFileTask(FileDescr localFile, FileDescr remoteDir) {
        this.localFile = localFile;
        this.remoteDir = remoteDir;
    }

    @Override
    protected Response call() throws Exception {
        NetworkClient networkClient = NetworkClient.getInstance();
        try (RandomAccessFile file = new RandomAccessFile(localFile.getAbsolutePath().toFile(), "r")) {
            //перенастроим путь
            localFile.setAbsolutePath(Paths.get(remoteDir.getPath(), localFile.getName()));
            FilePart filePart = new FilePart();
            SaveFileCommand saveFileCommand = new SaveFileCommand(localFile, filePart);
            int count;
            long position = 0;
            while ((count = file.read(filePart.getBytes())) != -1) {
                filePart.setStartPos(position);
                filePart.setLength(count);
                filePart.setDigest();
                position += count;
                file.seek(position);
                Response response;
                int tryNum = 0;
                do {
                    networkClient.invoke(saveFileCommand);
                    response = networkClient.getResponse();
                    updateProgress(position, localFile.getSize());
                    tryNum++;
                } while (response.getResponseCode() == FILE_CORRUPTED || tryNum > DefaultConfig.TRY_NUM);
                if (response.getResponseCode().isError()) {
                    throw new Exception(response.getResponseCode().getMessage());
                }
            }
        }
        return null;
    }
}
