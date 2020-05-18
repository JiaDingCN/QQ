package com.jiading.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-19 00:17
 **/
public class WarnPanelController {
    @FXML
    Button exit;
    @FXML
    Label Text;
    public void setText(String text){
        Text.setText(text);
    }
    public void exit(MouseEvent mouseEvent) {
    }
}
