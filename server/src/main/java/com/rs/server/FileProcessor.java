package com.rs.server;

import com.rs.common.DefaultConfig;
import com.rs.common.messages.Response;
import com.rs.common.messages.SaveFileCommand;
import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static com.rs.common.messages.ResponseCode.*;

public class FileProcessor {
    private UserContext userContext;

    private HashMap<FileDescriptor, FileInfo> files = new HashMap<>();

    class FileInfo {
        TempFile tempFile;
        Path targetPath;

        public FileInfo(String path, String name) throws IOException {
            this.targetPath =  getFilePath(path, name);
            tempFile = new TempFile(getFilePath(path, name+ DefaultConfig.PART_FILE_EXT).toFile(), "rw");
        }

        private Path getFilePath(String path, String name) throws IOException {
            Path p = Paths.get(userContext.getRootPath(), path).toRealPath(); //TODO сделать проверку на безопасность пути
            return Paths.get(p.toString(),name).toAbsolutePath();
        }

        public TempFile getTempFile() {
            return tempFile;
        }
    }

    public FileProcessor(UserContext userContext) {
        this.userContext = userContext;
    }

    //SAVE FILE
    public Response process(SaveFileCommand command) {
        Response response = new Response();
        if (!userContext.isAuthorized()) {
            response.setResponseCode(ACCESS_DENIED);
            return response;
        }
        try {
            FileInfo fileInfo;
            FileDescriptor fileDescriptor = command.getFileDescriptor();
            if (!files.containsKey(fileDescriptor)) {  //создаем запись о записываемом файле
                fileInfo = new FileInfo(fileDescriptor.getPath(), fileDescriptor.getName());
                files.put(fileDescriptor, fileInfo);
            } else {
                fileInfo = files.get(fileDescriptor);
            }
            FilePart filePart = command.getFilePart();
            //TODO проверить целостность
//            filePart.getBytes()[0] = 0;//поломаем
            if (filePart.damaged()) {
                response.setResponseCode(FILE_CORRUPTED);
                return response;
            } else {
                TempFile tempFile = fileInfo.getTempFile();
                tempFile.seek(filePart.getStartPos());
                tempFile.write(filePart.getBytes(), 0, filePart.getLength());
                if (tempFile.getFilePointer() == fileDescriptor.getSize()) {  //файл записан
                    tempFile.close();
                    Files.deleteIfExists(fileInfo.targetPath);
                    Files.move(tempFile.getPath(), fileInfo.targetPath);
                }
                response.setResponseCode(OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.setResponseCode(CANNOT_SAVE_FILE);
            response.setErrorDescription(e.getLocalizedMessage());
            return response;
        }
        return response;
    }
}
