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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    Logger logger = Logger.getRootLogger();

    private final double ROW_HEIGHT = 20.0;

    private ObservableList<FileDescriptor> localFilesList = FXCollections.observableArrayList();
    private ObservableList<FileDescriptor> remoteFilesList = FXCollections.observableArrayList();
    private FileDescriptor localDirectory = new FileDescriptor();
    private FileDescriptor remoteDirectory = new FileDescriptor();

    private Image dirImage = new Image(getClass().getResource("/folder.jpg").toString());

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
                doLocalEnter();
            }
        }
    }

    public void handleLocalTableKey(KeyEvent event) {
        System.out.println(event.getCode());
        if (event.getCode().equals(KeyCode.ENTER))
        {
            doLocalEnter();
        }
    }

    private void doLocalEnter() {
        FileDescriptor fileDescriptor = localFilesTable.getSelectionModel().getSelectedItem();
        if (fileDescriptor.isDirectory() && !fileDescriptor.getPath().equals("")) {
            localDirectory.setAbsolutePath(fileDescriptor.getAbsolutePath());
            updateLocalFileList();
        }
    }

    public void handleOnRemoteTableClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                doRemoteEnter();
            }
        }
    }

    public void handleRemoteTableKey(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            doRemoteEnter();
        }
    }

    private void doRemoteEnter() {
        FileDescriptor fileDescriptor = remoteFilesTable.getSelectionModel().getSelectedItem();
        if (fileDescriptor != null && fileDescriptor.isDirectory()) {
            remoteDirectory = fileDescriptor;
            updateRemoteFileList();
        }
    }
    //test
    public void handleSaveFileAction(ActionEvent actionEvent) {
        try {
            String fileName = fileName1.getText();
            String path = path1.getText();
            FileDescriptor fileDescriptor = new FileDescriptor();
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
            fileDescriptor.setPath(path);
            FileDescriptor newFileDescriptor = new FileDescriptor();
            newFileDescriptor.setPath(newPath);
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
            fileDescriptor.setPath(path);
            Worker.deleteFile(fileDescriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateLocalFileList() {
        try {
            System.out.println(localDirectory);
            ArrayList<FileDescriptor> list = FileUtilities.getRelativeDirectoryList(localDirectory);
            localFilesList.setAll(list);
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        dirImage= new HBox();
//        ImageView imageView = new ImageView();
//        imageView.setImage(new Image(getClass().getResource("/folder.jpg").toString()));
//        imageView.setFitHeight(30);
//        imageView.setPreserveRatio(true);
//        dirImage.getChildren().addAll(imageView);

        localDirectory.setAbsolutePath(Paths.get(DefaultConfig.CLIENT_ROOT_PATH));
        tableInit(localFilesTable);
        localFilesTable.setItems(localFilesList);
        tableInit(remoteFilesTable);
        remoteFilesTable.setItems(remoteFilesList);
        updateLocalFileList();
        updateRemoteFileList();
    }

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    private void tableInit(TableView<FileDescriptor> table) {

        TableColumn<FileDescriptor, String> tcName = (TableColumn<FileDescriptor, String>) table.getColumns().get(1);
        tcName.setCellValueFactory(new PropertyValueFactory<FileDescriptor, String>("name"));
        TableColumn<FileDescriptor, Long> tcSize = (TableColumn<FileDescriptor, Long>) table.getColumns().get(2);
        tcSize.setCellValueFactory(new PropertyValueFactory<FileDescriptor, Long>("size"));
        tcSize.setCellFactory(column -> new TableCell<FileDescriptor, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.equals("0")) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(ControllerHelper.formatSize(item.toString()));
                }
            }
        });
        TableColumn<FileDescriptor, Boolean> tcDirectory = (TableColumn<FileDescriptor, Boolean>) table.getColumns().get(0);
        tcDirectory.setCellValueFactory(new PropertyValueFactory<FileDescriptor, Boolean>("directory"));
        tcDirectory.setCellFactory(column -> new TableCell<FileDescriptor, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    setGraphic(null);
                } else {
                    if (item) {
                        HBox dirImageBox = new HBox();
                        ImageView imageView = new ImageView();
                        imageView.setImage(dirImage);
                        imageView.setFitHeight(ROW_HEIGHT);
                        imageView.setPreserveRatio(true);
                        dirImageBox.getChildren().addAll(imageView);
                        setGraphic(dirImageBox);
                    }
                }
            }
        });

//        table.setRowFactory(tv -> {
//            TableRow<FileDescriptor> row = new TableRow<>();
//
//            row.setOnDragDetected(event -> {
//                if (!row.isEmpty()) {
//                    FileDescriptor descriptor = row.getItem();
//                    Dragboard db = row.startDragAndDrop(TransferMode.COPY);
//                    db.setDragView(row.snapshot(null, null));
//                    ClipboardContent cc = new ClipboardContent();
//                    cc.put(SERIALIZED_MIME_TYPE, descriptor);
//                    db.setContent(cc);
//                    event.consume();
//                }
//            });
//
//            row.setOnDragOver(event -> {
//                Dragboard db = event.getDragboard();
//                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
//                    //if (row.getTableView() != ((FileDescriptor) db.getContent(SERIALIZED_MIME_TYPE));) {
//                    System.out.println("bingo!");
//                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
//                    event.consume();
//                    //}
//                }
//            });
//
//            row.setOnDragDropped(event -> {
//                Dragboard db = event.getDragboard();
////                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
////                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
////                    Person draggedPerson = tableView.getItems().remove(draggedIndex);
////
////                    int dropIndex;
////
////                    if (row.isEmpty()) {
////                        dropIndex = tableView.getItems().size();
////                    } else {
////                        dropIndex = row.getIndex();
////                    }
////
////                    tableView.getItems().add(dropIndex, draggedPerson);
////
////                    event.setDropCompleted(true);
////                    tableView.getSelectionModel().select(dropIndex);
////                    event.consume();
////                }
//            });
//
//            return row;
//        });
    }

}

