package com.rs.client;

import com.rs.common.messages.Command;
import com.rs.common.messages.SignInCommand;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegisterController extends LoginController {
    @FXML
    TextField email;

    @Override
    protected Command getCommand() {
        return new SignInCommand(loginField.getText(), passwordField.getText(), email.getText());
    }
}
