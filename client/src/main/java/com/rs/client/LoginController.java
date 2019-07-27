package com.rs.client;

import com.rs.client.tasks.SimpleNetworkTask;
import com.rs.common.messages.Command;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.Response;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    GridPane form;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    Label errorMsg;

    @FXML
    Button button;

    public void handleSubmitButton(ActionEvent actionEvent) {
        errorMsg.setText("");
        errorMsg.setVisible(false);
        button.setDisable(true);
        Task<Response> task = new SimpleNetworkTask(getCommand());
        task.setOnSucceeded(evt -> {
            Parent mainNode = null;
            try {
                mainNode = FXMLLoader.load(getClass().getResource("/mainForm.fxml"));
                ((Stage) form.getScene().getWindow()).setScene(new Scene(mainNode, ClientMain.WIDTH, ClientMain.HEIGHT));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        task.setOnFailed(evt -> {
            if (task.getException() instanceof Exception) {
                errorMsg.setText(task.getException().getLocalizedMessage());
                errorMsg.setVisible(true);
            }
            button.setDisable(false);
        });
        new Thread(task).start();
    }

    public void handleEnterButton(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            handleSubmitButton(new ActionEvent());
        }
    }

    protected Command getCommand() {
        return new LoginCommand(loginField.getText(), passwordField.getText());
    }

    public void handleSubmitRegisterButton(ActionEvent actionEvent) {
        errorMsg.setText("");
        errorMsg.setVisible(false);
        try {
            Parent registerNode = FXMLLoader.load(getClass().getResource("/registerForm.fxml"));
            ((Stage) form.getScene().getWindow()).setScene(new Scene(registerNode, ClientMain.WIDTH, ClientMain.HEIGHT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
