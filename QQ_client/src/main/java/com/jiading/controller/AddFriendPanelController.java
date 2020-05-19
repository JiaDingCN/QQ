package com.jiading.controller;

import com.jiading.domain.InfoUser;
import com.jiading.utils.PackageList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-19 16:43
 **/
public class AddFriendPanelController {
    @FXML
    Button submitButton;
    @FXML
    Button exitButton;
    @FXML
    TextField toUsernameField;
    String username;
    public void setUsername(String username){
        this.username=username;
    }
    public void submit(MouseEvent mouseEvent) throws IOException {
        InfoUser user=new InfoUser();
        user.setUsername(username);
        user.setToUsername(toUsernameField.getText());
        user.setInfoType(InfoUser.InfoTypes.ADDFRIEND.toString());
        PackageList.sendPackage(user);
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("WarnPanel.fxml"));
        Parent root = fxmlLoader.load();
        Stage newStage=new Stage();
        newStage.setTitle("好友申请已发送");
        newStage.setScene(new Scene(root, 640, 430));
        WarnPanelController controller=fxmlLoader.getController();
        controller.setText("您的好友申请已发送！");
        newStage.show();
        Stage window = (Stage) submitButton.getScene().getWindow();
        window.close();
    }

    public void exit(MouseEvent mouseEvent) {
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.close();
    }
}
