package com.jiading.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-19 00:40
 **/
public class ChatPanelController {
    @FXML
    TextArea messageField;
    @FXML
    TextArea inputField;
    @FXML
    Button submitButton;
    @FXML
    Button exitButton;
    @FXML
    Label chatInfoText;
    public void submit(MouseEvent mouseEvent) {
    }

    public void exit(MouseEvent mouseEvent) {
    }
}
