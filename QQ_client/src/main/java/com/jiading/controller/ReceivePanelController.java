package com.jiading.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-20 10:57
 **/
public class ReceivePanelController {
    @FXML
    Label titleLabel;
    @FXML
    Label pathLabel;
    @FXML
    Button chooseFolder;
    @FXML
    Button receiveButton;
    @FXML
    Button exitButton;
    @FXML
    Label processLabel;
    public void chooseFolder(MouseEvent mouseEvent) {
    }

    public void receive(MouseEvent mouseEvent) {
    }

    public void exit(MouseEvent mouseEvent) {
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.close();
    }
}
