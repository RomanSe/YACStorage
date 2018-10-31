package com.rs.client;

import com.rs.client.tasks.GetRemoteDirectoryTask;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
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

    public void handleOnLocalTableClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                FileDescriptor fileDescriptor = localFilesTable.getSelectionModel().getSelectedItem();
                if (fileDescriptor.isDirectory()) {
                    localDirectory.setAbsolutePath(fileDescriptor.getAbsolutePath());
                    updateLocalFileList();
                }
            }
        }
    }

    public void handleOnRemoteTableClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                FileDescriptor fileDescriptor = remoteFilesTable.getSelectionModel().getSelectedItem();
                if (fileDescriptor != null && fileDescriptor.isDirectory()) {
                    System.out.println(fileDescriptor.getAbsolutePath());
                    remoteDirectory = fileDescriptor;
                    remoteDirectory.normalize();
                    updateRemoteFileList();
                }
            }
        }
    }


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

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");


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
        Task<Response> task = new GetRemoteDirectoryTask(remoteDirectory, remoteFilesList);
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

    private void tableInit(TableView<FileDescriptor> table) {

        TableColumn<FileDescriptor, String> tcName = (TableColumn<FileDescriptor, String>) table.getColumns().get(1);
        tcName.setCellValueFactory(new PropertyValueFactory<FileDescriptor, String>("name"));
        TableColumn<FileDescriptor, String> tcSize = (TableColumn<FileDescriptor, String>) table.getColumns().get(2);
        tcSize.setCellValueFactory(new PropertyValueFactory<FileDescriptor, String>("size"));
        TableColumn<FileDescriptor, String> tcDirectory = (TableColumn<FileDescriptor, String>) table.getColumns().get(0);
        tcDirectory.setCellValueFactory(new PropertyValueFactory<FileDescriptor, String>("d"));

        table.setRowFactory(tv -> {
            TableRow<FileDescriptor> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    FileDescriptor descriptor = row.getItem();
                    Dragboard db = row.startDragAndDrop(TransferMode.COPY);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, descriptor);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    //if (row.getTableView() != ((FileDescriptor) db.getContent(SERIALIZED_MIME_TYPE));) {
                        System.out.println("bingo!");
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    //}
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
//                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
//                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
//                    Person draggedPerson = tableView.getItems().remove(draggedIndex);
//
//                    int dropIndex;
//
//                    if (row.isEmpty()) {
//                        dropIndex = tableView.getItems().size();
//                    } else {
//                        dropIndex = row.getIndex();
//                    }
//
//                    tableView.getItems().add(dropIndex, draggedPerson);
//
//                    event.setDropCompleted(true);
//                    tableView.getSelectionModel().select(dropIndex);
//                    event.consume();
//                }
            });

            return row;
        });
    }

}

