package com.jiading.controller;

import com.jiading.domain.InfoUser;
import com.jiading.utils.PackageList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.Thread.sleep;

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
    public void submit(MouseEvent mouseEvent) throws InterruptedException, IOException {
        InfoUser user=new InfoUser();
        user.setPassword(passwordField.getText());
        user.setUsername(usernameField.getText());
        user.setInfoType(InfoUser.InfoTypes.SIGNIN.toString());
        PackageList.sendPackage(user);
        while(!PackageList.hasPackagesToReceive()){
            sleep(500);
        }
        InfoUser receivedUser=PackageList.getReceivedPackage();
        if(receivedUser.getChatInfo().equals(InfoUser.stateCodes.SIGNINFALL.toString())){
            //失败
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("WarnPanel.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage=new Stage();
            newStage.setTitle("密码错误");
            newStage.setScene(new Scene(root, 640, 430));
            WarnPanelController controller = fxmlLoader.getController();
            controller.setText("密码或者用户名错误！请重新输入");
            newStage.show();
        }else{
            //成功
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("MainPanel.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage=new Stage();
            newStage.setTitle("主菜单");
            newStage.setScene(new Scene(root, 340, 655));
            MainPanelController controller=fxmlLoader.getController();
            controller.setInfo(usernameField.getText(),receivedUser.getChatInfo(),passwordField.getText());
            newStage.show();
            Stage window = (Stage) submitButton.getScene().getWindow();
            window.close();
        }
    }

    public void exit(MouseEvent mouseEvent) {
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.close();
    }
}
