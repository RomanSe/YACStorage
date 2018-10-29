package com.rs.client;

import com.rs.common.DefaultConfig;
import com.rs.common.FileUtilities;
import com.rs.common.model.FileDescriptor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private FileDescriptor currentDirectory = new FileDescriptor();
    private Path rootPath = Paths.get(DefaultConfig.CLIENT_ROOT_PATH);

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateLocalFileList();
        tableInit(localFilesTable);
        localFilesTable.setItems(localFilesList);

        tableInit(remoteFilesTable);
        remoteFilesTable.setItems(remoteFilesList);
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
        Path localPath = FileUtilities.getFilePath(DefaultConfig.CLIENT_ROOT_PATH, currentDirectory.getPath(), currentDirectory.getName());
        try {
            localFilesList.clear();
            localFilesList.addAll(FileUtilities.getRelativeDirectoryList(localPath, rootPath));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }
}

