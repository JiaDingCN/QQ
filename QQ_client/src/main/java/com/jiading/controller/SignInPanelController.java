package com.jiading.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-18 23:47
 **/
public class SignInPanelController {
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button submitButton;
    @FXML
    Button exitButton;
    public void submit(MouseEvent mouseEvent) {
    }

    public void exit(MouseEvent mouseEvent) {
    }
}
