package com.rs.client;

import com.rs.client.tasks.GetRemoteDirectoryTask;
import com.rs.client.tasks.SaveFileTask;
import com.rs.common.FileUtilities;
import com.rs.common.messages.Response;
import com.rs.common.model.FileDescriptor;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class ControllerHelper {
    static Logger logger = Logger.getRootLogger();
    static MainFormController c;

    static void updateRemoteFileList() {
        FileDescriptor file = c.remoteFilesTable.getSelectionModel().getSelectedItem();
        if (file != null && file.isDirectory()) {
            c.remoteDirectory = file;
        }
        c.errorMsg.setText("");
        c.errorMsg.setVisible(false);
        Task<Response> task = new GetRemoteDirectoryTask(c.remoteDirectory, c.remoteFilesList);
        task.setOnSucceeded(evt -> {
            c.errorMsg.setText("");
        });
        task.setOnFailed(evt -> {
            if (task.getException() instanceof Exception) {
                c.errorMsg.setText(task.getException().getLocalizedMessage());
                c.errorMsg.setVisible(true);
            }
        });
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    static void updateLocalFileList() {
        FileDescriptor file = c.localFilesTable.getSelectionModel().getSelectedItem();
        if (file != null && file.isDirectory() && !file.getPath().equals("")) {
            c.localDirectory.setAbsolutePath(file.getAbsolutePath());
        }
        try {
            System.out.println(c.localDirectory);
            ArrayList<FileDescriptor> list = FileUtilities.getRelativeDirectoryList(c.localDirectory);
            c.localFilesList.setAll(list);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

    }

    static void saveFile() {
        showError(null);
        FileDescriptor fileDescriptor = c.localFilesTable.getSelectionModel().getSelectedItem();
        if (fileDescriptor.isDirectory()) {
            showError("Копирование директорий не поддерживается");
            return;
        }

        // Показывает Alert с возможностью нажатия одной из двух кнопок
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Загрузить на сервер " + fileDescriptor.getName() + "?", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().getText().equals("Cancel")) {
            return;
        }
        //перекодирование пути файла
        FileDescriptor remoteFile = new FileDescriptor();
        remoteFile.setRoot(fileDescriptor.getParent());
        remoteFile.setAbsolutePath(Paths.get(fileDescriptor.getPath()));
        remoteFile.setSize(fileDescriptor.getSize());

        Task<Response> task = new SaveFileTask(remoteFile);
        c.progressBar.progressProperty().bind(task.progressProperty());
        task.setOnScheduled(e -> c.progressStage.show());
        task.setOnSucceeded(evt -> {
            showError(null);
        });
        task.setOnFailed(evt -> {
            if (task.getException() instanceof Exception) {
                showError(task.getException().getLocalizedMessage());
                c.progressStage.hide();
            }
        });
        Thread th = new Thread(task);
        //th.setDaemon(true);
        th.start();
    }

    private static void showError(String error) {
        if (error != null) {
            c.errorMsg.setText(error);
            c.errorMsg.setVisible(true);
        } else {
            c.errorMsg.setVisible(false);
        }
    }


    public static String formatSize(String value) {
        long size = Long.parseLong(value);
        if (size < 1000)
            return value + " B";
        else if (size < 1000000)
            return (size / 1000) + " kB";
        else if (size < 1000000000)
            return (size / 1000000) + " MB";
        else
            return (size / 1000000000) + " GB";
    }

    public static void init(MainFormController mainFormController) {
        c = mainFormController;
    }
}
