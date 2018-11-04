package com.rs.client;

import com.rs.common.DefaultConfig;
import com.rs.common.model.FileDescriptor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    Logger logger = Logger.getRootLogger();

    private final double ROW_HEIGHT = 20.0;

    protected ObservableList<FileDescriptor> localFilesList = FXCollections.observableArrayList();
    protected ObservableList<FileDescriptor> remoteFilesList = FXCollections.observableArrayList();
    protected FileDescriptor localDirectory = new FileDescriptor();
    protected FileDescriptor remoteDirectory = new FileDescriptor();

    protected Stage progressStage;
    protected ProgressBar progressBar;

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
                ControllerHelper.updateLocalFileList();            }
        }
    }

    public void handleLocalTableKey(KeyEvent event) {
        System.out.println(event.getCode());
        switch (event.getCode()){
            case ENTER:
                ControllerHelper.updateLocalFileList();
                break;
            case F5:
                ControllerHelper.saveFile();
        }
    }

    public void handleOnRemoteTableClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                ControllerHelper.updateRemoteFileList();
            }
        }
    }

    public void handleRemoteTableKey(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            ControllerHelper.updateRemoteFileList();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localDirectory.setAbsolutePath(Paths.get(DefaultConfig.CLIENT_ROOT_PATH));
        ControllerHelper.init(this);
        initProgressBar();
        tableInit(localFilesTable);
        tableInit(remoteFilesTable);
        localFilesTable.setItems(localFilesList);
        remoteFilesTable.setItems(remoteFilesList);
        ControllerHelper.updateLocalFileList();
        ControllerHelper.updateRemoteFileList();
    }

//    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    public void initProgressBar() {
        progressBar = new ProgressBar();
        progressStage = new Stage();
        progressStage.setScene(new Scene(new StackPane(progressBar), 300, 300));
        progressStage.setAlwaysOnTop(true);
    }

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

