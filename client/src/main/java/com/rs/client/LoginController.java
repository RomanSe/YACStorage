package com.rs.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    Label loginField;

    @FXML
    PasswordField passwordField;

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        //TODO добавить вызов авторизации
        ((Stage) Main.loginForm.getWindow()).setScene(Main.mainForm);
    }
}
