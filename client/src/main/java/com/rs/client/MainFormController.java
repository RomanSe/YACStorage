package com.rs.client;

import com.rs.common.DefaultConfig;
import com.rs.common.model.FileDescriptor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.File;
import java.nio.file.Paths;

public class MainFormController {
    @FXML
    TextField path1;

    @FXML
    TextField path2;

    @FXML
    TextField fileName1;

    @FXML
    TextField fileName2;


    //test
    public void handleSaveFileAction(ActionEvent actionEvent) {
        try {
            String fileName = fileName1.getText();
            String path = path1.getText();
            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setName(fileName);
            fileDescriptor.setPath(path);
            fileDescriptor.setAbsolutePath(Paths.get(DefaultConfig.CLIENT_ROOT_PATH, path, fileName));
            File file = fileDescriptor.getAbsolutePath().toFile();
            fileDescriptor.setSize(file.length());
            Worker.saveFile(fileDescriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleDownloadFileAction(ActionEvent actionEvent) {
        try {
            String fileName = fileName1.getText();
            String path = path1.getText();
            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setName(fileName);
            fileDescriptor.setPath(path);
            Worker.downloadFile(fileDescriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleRenameFileAction(ActionEvent actionEvent) {
        try {
            String fileName = fileName1.getText();
            String path = path1.getText();
            String newFileName = fileName2.getText();
            String newPath = path2.getText();
            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setName(fileName);
            fileDescriptor.setPath(path);
            FileDescriptor newFileDescriptor = new FileDescriptor();
            newFileDescriptor.setName(newFileName);
            newFileDescriptor.setPath(newPath);
            Worker.moveFile(fileDescriptor, newFileDescriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleGetDirectoryAction(ActionEvent actionEvent) {
        try {
            String fileName = fileName1.getText();
            String path = path1.getText();
            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setName(fileName);
            fileDescriptor.setPath(path);
            Worker.getDirectory(fileDescriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleDeleteAction(ActionEvent actionEvent) {
        try {
            String fileName = fileName1.getText();
            String path = path1.getText();
            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setName(fileName);
            fileDescriptor.setPath(path);
            Worker.deleteFile(fileDescriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
