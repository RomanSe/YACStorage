package com.rs.client;

import com.rs.common.DefaultConfig;
import com.rs.common.model.FileDescriptor;
import javafx.event.ActionEvent;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainFormController {

    //test
    public void handleSaveFileAction(ActionEvent actionEvent) {
        try {
            String fileName = "assa2.mkv";
            String path = "";
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

}
