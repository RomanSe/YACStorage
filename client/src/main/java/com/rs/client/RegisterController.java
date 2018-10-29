package com.rs.client;

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

public class RegisterController {
    @FXML
    GridPane registerForm;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    TextField email;

    @FXML
    Label errorMsg;

    @FXML
    Button signInButton;

    public void handleSubmitSignInButton(ActionEvent actionEvent) {
        errorMsg.setText("");
        errorMsg.setVisible(false);
        try {
            signInButton.setDisable(true);
            Worker.signIn(loginField.getText(), passwordField.getText(), email.getText());
            Parent mainNode = FXMLLoader.load(getClass().getResource("/mainForm.fxml"));
            ((Stage) registerForm.getScene().getWindow()).setScene(new Scene(mainNode, Main.WIDTH, Main.HEIGHT));
        } catch (Exception e) {
            errorMsg.setText(e.getLocalizedMessage());
            errorMsg.setVisible(true);
        } finally {
            signInButton.setDisable(false);
        }

    }
}
