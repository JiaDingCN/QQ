package com.jiading.controller;

import com.jiading.domain.Friend;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-19 00:36
 **/
public class MainPanelController {
    @FXML
    TableView<Friend>friendList;
    @FXML
    TableColumn<Friend,String>usernameColumn;
    @FXML
    TableColumn<Friend,String>statusColumn;
    @FXML
    Button exitButton;
    public void exit(MouseEvent mouseEvent) {
    }
}
