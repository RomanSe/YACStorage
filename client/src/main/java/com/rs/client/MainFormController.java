package com.rs.client;

import javafx.event.ActionEvent;

public class MainFormController {


    //test
    public void handleSaveFileAction(ActionEvent actionEvent) {
        try {
            Worker.saveFile("123.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
