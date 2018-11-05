package com.rs.client;

import com.rs.client.tasks.DownloadFileTask;
import com.rs.client.tasks.GetRemoteDirectoryTask;
import com.rs.client.tasks.SaveFileTask;
import com.rs.client.tasks.SimpleNetworkTask;
import com.rs.common.FileUtilities;
import com.rs.common.messages.DeleteFileCommand;
import com.rs.common.messages.MoveCommand;
import com.rs.common.messages.Response;
import com.rs.common.model.FileDescr;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Optional;

public class ControllerHelper {
    static Logger logger = Logger.getLogger(ControllerHelper.class);
    static MainFormController c;

    static void deleteRemoteFile() {
        FileDescr file = c.remoteFilesTable.getSelectionModel().getSelectedItem();
        Task<Response> task = new SimpleNetworkTask(new DeleteFileCommand(file));
        invokeSimpleTask(task, "Удалить файл " + file.getName() + " ?");
    }

    static void renameRemoteFile() {
        FileDescr file = c.remoteFilesTable.getSelectionModel().getSelectedItem();
        String newName = showTextInputDialog("Переименование", file.getName(), "Новое имя:");
        FileDescr newFile = new FileDescr();
        newFile.setPath(FileUtilities.changeFileName(file.getPath(), newName));
        Task<Response> task = new SimpleNetworkTask(new MoveCommand(file, newFile));
        invokeSimpleTask(task, "Переименовать файл " + file.getName() + " ?");
    }

    static void deleteLocalFile() {
        FileDescr file = c.localFilesTable.getSelectionModel().getSelectedItem();
        if (!getConfirmation("Удалить файл " + file.getName() + " ?")) return;
        try {
            Files.delete(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            showError(e.toString());
        }
        updateLocalFileList();
    }

    static void updateRemoteFileList() {
        FileDescr file = c.remoteFilesTable.getSelectionModel().getSelectedItem();
        if (file != null && file.isDirectory()) {
            c.remoteDirectory = file;
        }
        showError(null);
        Task<Response> task = new GetRemoteDirectoryTask(c.remoteDirectory, c.remoteFilesList);
        task.setOnFailed(evt -> {
            if (task.getException() instanceof Exception) {
                showError(task.getException().getLocalizedMessage());
            }
        });
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    static void updateLocalFileList() {
        FileDescr file = c.localFilesTable.getSelectionModel().getSelectedItem();
        if (file != null && file.isDirectory() && !file.getPath().equals("")) {
            c.localDirectory.setAbsolutePath(file.getAbsolutePath());
        }
        try {
            ArrayList<FileDescr> list = FileUtilities.getRelativeDirectoryList(c.localDirectory);
            c.localFilesList.setAll(list);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

    }

    static void saveFile() {
        FileDescr fileDescr = c.localFilesTable.getSelectionModel().getSelectedItem();
        if (fileDescr.isDirectory()) {
            showError("Копирование директорий не поддерживается");
            return;
        }
        longTask("Загрузить в хранилище " + fileDescr.getName() + "?",
                "Загрузка в хранилище: " + fileDescr.getName(),
                new SaveFileTask(fileDescr, c.remoteDirectory));

    }

    static void downloadFile() {
        FileDescr fileDescr = c.remoteFilesTable.getSelectionModel().getSelectedItem();
        if (fileDescr.isDirectory()) {
            showError("Копирование директорий не поддерживается");
            return;
        }
        longTask("Выгрузить из хранилища " + fileDescr.getName() + "?",
                "Выгрузка из хранилища: " + fileDescr.getName(),
                new DownloadFileTask(fileDescr, c.localDirectory));
    }

    static void longTask(String alertText, String progressText, Task<Response> task) {
        if (!getConfirmation(alertText)) return;
        showError(null);
        getProgressBar().progressProperty().bind(task.progressProperty());
        getProgressBarLabel().setText(progressText);
        task.setOnScheduled(e -> c.progressStage.show());
        task.setOnSucceeded(evt -> {
            showError(null);
            c.progressStage.hide();
            updateLocalFileList();
            updateRemoteFileList();
        });
        task.setOnFailed(evt -> {
            if (task.getException() instanceof Exception) {
                showError(task.getException().getLocalizedMessage());
                c.progressStage.hide();
            }
        });
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }


    private static void invokeSimpleTask(Task<Response> task, String alertText) {
        if (!getConfirmation(alertText)) return;

        task.setOnSucceeded(evt -> {
            updateRemoteFileList();
            updateLocalFileList();
        });
        task.setOnFailed(evt -> {
            if (task.getException() instanceof Exception) {
                showError(task.getException().getLocalizedMessage());
            }
        });
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private static boolean getConfirmation(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText(alertMessage);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().getText().equals("Cancel")) {
            return false;
        }
        return true;
    }

    private static void showError(String error) {
        if (error != null) {
            c.errorMsg.setText(error);
            c.errorMsg.setVisible(true);
        } else {
            c.errorMsg.setVisible(false);
        }
    }

    private static String showTextInputDialog(String title, String dflt, String text) {
        TextInputDialog dialog = new TextInputDialog(dflt);
        dialog.setTitle(title);
        dialog.setContentText(text);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        }
        return null;
    }

    private static ProgressBar getProgressBar() {
        return (ProgressBar) c.progressStage.getScene().lookup("#progressBar");
    }

    private static Label getProgressBarLabel() {
        return (Label) c.progressStage.getScene().lookup("#label");
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
