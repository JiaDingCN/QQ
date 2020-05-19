package com.jiading.controller;

import com.jiading.service.SocketService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-18 23:14
 **/
public class SignPanelController {
    @FXML
    Button SignUpButton;
    @FXML
    Button SignInButton;
    /*public void start(){
        SocketService socketService=new SocketService("192.168.1.4",6768);
        socketService.start();
    }*/
    public void toSignUpPanel(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("SignUpPanel.fxml"));
        Parent root = fxmlLoader.load();
        Stage newStage=new Stage();
        newStage.setTitle("注册");
        newStage.setScene(new Scene(root, 640, 430));
        newStage.show();
        exit(SignUpButton);
    }

    public void toSignInPanel(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("SignInPanel.fxml"));
        Parent root = fxmlLoader.load();
        Stage newStage=new Stage();
        newStage.setTitle("登录");
        newStage.setScene(new Scene(root, 640, 430));
        newStage.show();
        exit(SignInButton);
    }
    public void exit(Button button){
        Stage window = (Stage) button.getScene().getWindow();
        window.close();
    }
}
