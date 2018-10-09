package com.rs.client;

import com.rs.client.network.NetworkClient;
import com.rs.common.DefaultConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    Label errorMsg;

    public void handleSubmitLoginButton(ActionEvent actionEvent) {
        errorMsg.setText("");
        errorMsg.setVisible(false);
        try {
            Worker.login(loginField.getText(), passwordField.getText());
            ((Stage) Main.loginForm.getWindow()).setScene(Main.mainForm);
        } catch (Exception e) {
            errorMsg.setText(e.getLocalizedMessage());
            errorMsg.setVisible(true);
        }

    }
}
