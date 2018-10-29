package com.rs.client;

import com.rs.client.network.NetworkClient;
import com.rs.common.DefaultConfig;
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

import java.io.IOException;

public class LoginController {
    @FXML
    GridPane loginForm;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    Label errorMsg;

    @FXML
    Button loginButton;

    public void handleSubmitLoginButton(ActionEvent actionEvent) {
        errorMsg.setText("");
        errorMsg.setVisible(false);
        try {
            loginButton.setDisable(true);
            Worker.login(loginField.getText(), passwordField.getText());
            Parent mainNode = FXMLLoader.load(getClass().getResource("/mainForm.fxml"));
            ((Stage) loginForm.getScene().getWindow()).setScene(new Scene(mainNode, Main.WIDTH, Main.HEIGHT));
        } catch (Exception e) {
            errorMsg.setText(e.getLocalizedMessage());
            errorMsg.setVisible(true);
        } finally {
            loginButton.setDisable(false);
        }
    }

    public void handleSubmitRegisterButton(ActionEvent actionEvent) {
        errorMsg.setText("");
        errorMsg.setVisible(false);
        try {
            Parent registerNode = FXMLLoader.load(getClass().getResource("/registerForm.fxml"));
            ((Stage) loginForm.getScene().getWindow()).setScene(new Scene(registerNode, Main.WIDTH, Main.HEIGHT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
