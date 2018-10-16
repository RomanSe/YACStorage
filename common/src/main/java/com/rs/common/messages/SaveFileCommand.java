package com.rs.common.messages;

import com.rs.common.Context;
import com.rs.common.DefaultConfig;
import com.rs.common.FileProcessingInfo;
import com.rs.common.model.FileDescriptor;
import com.rs.common.model.FilePart;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.rs.common.messages.ResponseCode.*;

public class SaveFileCommand extends Command {
    private static final long serialVersionUID = -114964002600995666L;
    private FileDescriptor fileDescriptor;

    public FilePart getFilePart() {
        return filePart;
    }

    public void setFilePart(FilePart filePart) {
        this.filePart = filePart;
    }

    private FilePart filePart;

    public SaveFileCommand(FileDescriptor fileDescriptor, FilePart filePart) {
        this.fileDescriptor = fileDescriptor;
        this.filePart = filePart;
    }

    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    //////////////////////////////////////////////  TODO реорганизовать код, чтобы вынести это в серверную часть

    public Response process(Context context) {
        Response response = Response.getInstance();
        if (!context.isAuthorized()) {
            response.setResponseCode(ACCESS_DENIED);
            return response;
        }
        try {
            FileProcessingInfo fileInfo = context.getFileProcessingInfo();
            Path targetFilePath = getFilePath(context.getRootPath(), fileDescriptor.getPath(), fileDescriptor.getName());
            if (fileInfo == null || !fileInfo.getTargetPath().equals(targetFilePath)) {  //информация о другом файле или файла не было
                if (fileInfo != null) {
                    fileInfo.getTempFile().close();
                    Files.deleteIfExists(fileInfo.getTargetPath()); //сначала удалим предыдущий временный файл
                } else {
                    fileInfo = new FileProcessingInfo();
                    context.setFileProcessingInfo(fileInfo);
                }
                fileInfo.setTargetPath(targetFilePath);
                fileInfo.setTempFilePath(getFilePath(context.getRootPath(), fileDescriptor.getPath(), fileDescriptor.getName() + DefaultConfig.PART_FILE_EXT));
                if (fileInfo.getTempFilePath() != null)
                    Files.deleteIfExists(fileInfo.getTargetPath()); //удалим временный файл
                fileInfo.setTempFile(new RandomAccessFile(fileInfo.getTempFilePath().toFile(), "rw"));
            }
            //TODO проверить целостность
//            filePart.getBytes()[0] = 0;//поломаем
            if (filePart.damaged()) {
                response.setResponseCode(FILE_CORRUPTED);
                return response;
            } else {
                RandomAccessFile tempFile = fileInfo.getTempFile();
                tempFile.seek(filePart.getStartPos());
                tempFile.write(filePart.getBytes(), 0, filePart.getLength());
                if (tempFile.getFilePointer() == fileDescriptor.getSize()) {  //файл записан
                    tempFile.close();
                    if (Files.exists(fileInfo.getTargetPath()))
                        Files.delete(fileInfo.getTargetPath());
                    Files.move(fileInfo.getTempFilePath(), fileInfo.getTargetPath());
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

    public static Path getFilePath(String rootPath, String path, String name) throws IOException {
        Path p = Paths.get(rootPath, path).toRealPath(); //TODO сделать проверку на безопасность пути
        return Paths.get(p.toString(), name).toAbsolutePath();
    }

}
