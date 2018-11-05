package com.rs.client;

import com.rs.common.DefaultConfig;
import com.rs.common.model.FileDescr;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    Logger logger = Logger.getRootLogger();

    private final double ROW_HEIGHT = 20.0;

    protected ObservableList<FileDescr> localFilesList = FXCollections.observableArrayList();
    protected ObservableList<FileDescr> remoteFilesList = FXCollections.observableArrayList();
    protected FileDescr localDirectory = new FileDescr();
    protected FileDescr remoteDirectory = new FileDescr();

    protected Stage progressStage;
    protected Stage textLineStage;


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
    TableView<FileDescr> localFilesTable;

    @FXML
    TableView<FileDescr> remoteFilesTable;

    @FXML
    Label errorMsg;

    public void handleOnLocalTableClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                ControllerHelper.updateLocalFileList();
            }
        }
        mouseEvent.consume();
    }

    public void handleLocalTableKey(KeyEvent event) {
        System.out.println(event.getCode());
        switch (event.getCode()) {
            case ENTER:
                ControllerHelper.updateLocalFileList();
                break;
            case F5:
                ControllerHelper.saveFile();
                break;
            case F8:
                ControllerHelper.deleteLocalFile();
                break;
        }
        event.consume();
    }

    public void handleOnRemoteTableClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                ControllerHelper.updateRemoteFileList();
            }
        }
        mouseEvent.consume();
    }

    public void handleRemoteTableKey(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER:
                ControllerHelper.updateRemoteFileList();
                break;
            case F5:
                ControllerHelper.downloadFile();
                break;
            case F8:
                ControllerHelper.deleteRemoteFile();
                break;
            case F6:
                ControllerHelper.renameRemoteFile();
                break;
        }
        event.consume();
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
        Parent node = null;
        try {
            node = FXMLLoader.load(getClass().getResource("/progressForm.fxml"));
            progressStage = new Stage();
            progressStage.setScene(new Scene(node));
            progressStage.setAlwaysOnTop(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTextDialog() {
        Parent node = null;
        try {
            node = FXMLLoader.load(getClass().getResource("/progressForm.fxml"));
            progressStage = new Stage();
            progressStage.setScene(new Scene(node));
            progressStage.setAlwaysOnTop(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void tableInit(TableView<FileDescr> table) {

        TableColumn<FileDescr, String> tcName = (TableColumn<FileDescr, String>) table.getColumns().get(1);
        tcName.setCellValueFactory(new PropertyValueFactory<FileDescr, String>("name"));
        TableColumn<FileDescr, Long> tcSize = (TableColumn<FileDescr, Long>) table.getColumns().get(2);
        tcSize.setCellValueFactory(new PropertyValueFactory<FileDescr, Long>("size"));
        tcSize.setCellFactory(column -> new TableCell<FileDescr, Long>() {
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
        TableColumn<FileDescr, Boolean> tcDirectory = (TableColumn<FileDescr, Boolean>) table.getColumns().get(0);
        tcDirectory.setCellValueFactory(new PropertyValueFactory<FileDescr, Boolean>("directory"));
        tcDirectory.setCellFactory(column -> new TableCell<FileDescr, Boolean>() {
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
    }

}

