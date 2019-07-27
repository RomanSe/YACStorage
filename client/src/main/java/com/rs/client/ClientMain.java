package com.rs.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.PropertyConfigurator;


public class ClientMain extends Application {
    static final int WIDTH = 600;
    static final int HEIGHT = 550;
    static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Parent loginNode = FXMLLoader.load(getClass().getResource("/loginForm.fxml"));
        Scene loginForm = new Scene(loginNode, WIDTH, HEIGHT);

        primaryStage.setTitle("YACStorage");
        primaryStage.setScene(loginForm);
        primaryStage.show();
    }

    public static void configureLogger() {
        String nameFile = "log4j.xml";
        PropertyConfigurator.configure(nameFile);
    }

    public static void main(String[] args) {
        configureLogger();
        launch(args);
    }
}
