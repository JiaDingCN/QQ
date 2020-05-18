package com.jiading;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-18 23:38
 **/
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("SignPanel.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("登录或注册");
        primaryStage.setScene(new Scene(root, 640, 430));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

