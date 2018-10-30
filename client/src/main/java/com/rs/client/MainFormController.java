package com.rs.client;

import com.rs.client.tasks.GetDirectoryTask;
import com.rs.common.DefaultConfig;
import com.rs.common.FileUtilities;
import com.rs.common.messages.Response;
import com.rs.common.model.FileDescriptor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    Logger logger = Logger.getRootLogger();

    private ObservableList<FileDescriptor> localFilesList = FXCollections.observableArrayList();
    private ObservableList<FileDescriptor> remoteFilesList = FXCollections.observableArrayList();
    private FileDescriptor localDirectory = new FileDescriptor();
    private FileDescriptor remoteDirectory = new FileDescriptor();

    @FXML
    TextField path1;

    @FXML
    TextField path2;

    @FXML
    TextField fileName1;

    @FXML
    TextField fileName2;

    @FXML
    TableView<FileDescriptor> localFilesTable;

    @FXML
    TableView<FileDescriptor> remoteFilesTable;

    @FXML
    Label errorMsg;

    //test
    public void handleSaveFileAction(ActionEvent actionEvent) {
        try {
            String fileName = fileName1.getText();
            String path = path1.getText();
            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setName(fileName);
            fileDescriptor.setRelativePath(path);
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
            fileDescriptor.setRelativePath(path);
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
            fileDescriptor.setRelativePath(path);
            FileDescriptor newFileDescriptor = new FileDescriptor();
            newFileDescriptor.setName(newFileName);
            newFileDescriptor.setRelativePath(newPath);
            Worker.moveFile(fileDescriptor, newFileDescriptor);
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
            fileDescriptor.setRelativePath(path);
            Worker.deleteFile(fileDescriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localDirectory.setAbsolutePath(Paths.get(DefaultConfig.CLIENT_ROOT_PATH));
        tableInit(localFilesTable);
        localFilesTable.setItems(localFilesList);
        tableInit(remoteFilesTable);
        remoteFilesTable.setItems(remoteFilesList);
        updateLocalFileList();
        updateRemoteFileList();
    }

    private void tableInit(TableView<FileDescriptor> table) {
        TableColumn<FileDescriptor, String> tcName = (TableColumn<FileDescriptor, String>) table.getColumns().get(1);
        tcName.setCellValueFactory(new PropertyValueFactory<FileDescriptor, String>("name"));
        TableColumn<FileDescriptor, String> tcSize = (TableColumn<FileDescriptor, String>) table.getColumns().get(2);
        tcSize.setCellValueFactory(new PropertyValueFactory<FileDescriptor, String>("size"));
        TableColumn<FileDescriptor, String> tcDirectory = (TableColumn<FileDescriptor, String>) table.getColumns().get(0);
        tcDirectory.setCellValueFactory(new PropertyValueFactory<FileDescriptor, String>("d"));
    }

    private void updateLocalFileList() {
        Path localPath = localDirectory.getAbsolutePath();
        try {
            localFilesList.clear();
            localFilesList.addAll(FileUtilities.getRelativeDirectoryList(localDirectory.getAbsolutePath(), Paths.get("")));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    public void updateRemoteFileList() {
        errorMsg.setText("");
        errorMsg.setVisible(false);
        Task<Response> task = new GetDirectoryTask(remoteDirectory, remoteFilesList);
        task.setOnSucceeded(evt -> {
            errorMsg.setText("");
        });
        task.setOnFailed(evt -> {
            if (task.getException() instanceof Exception) {
                errorMsg.setText(task.getException().getLocalizedMessage());
                errorMsg.setVisible(true);
            }
        });
        new Thread(task).start();
    }
}

