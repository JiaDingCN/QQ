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
 * @create: 2020-05-19 00:24
 **/
public class SignUpPanelCodeController {
    @FXML
    TextField codeField;
    @FXML
    Button submitButton;
    @FXML
    Button exitButton;
    InfoUser user;
    @FXML
    PasswordField passwordField;
    public void setInfoUser(InfoUser user){
        this.user=user;
    }
    public void setPasswordField(PasswordField passwordField){
        this.passwordField=passwordField;
    }

    public void submit(MouseEvent mouseEvent) throws InterruptedException, IOException {
        user.setCode(codeField.getText());
        user.setChatInfo(InfoUser.stateCodes.NEEDVERIFY.toString());
        user.setInfoType(InfoUser.InfoTypes.SIGNUP.toString());
        PackageList.sendPackage(user);
        while (!PackageList.hasPackagesToReceive()) {
            sleep(500);
        }
        InfoUser receivedUser = PackageList.getReceivedPackage();
        if (receivedUser.getChatInfo().equals(InfoUser.stateCodes.SIGNUUPSUCCESS.toString())) {
            //注册成功
            FXMLLoader fxmlLoader1=new FXMLLoader(getClass().getClassLoader().getResource("SignInPanel.fxml"));
            Parent root1 = fxmlLoader1.load();
            Stage newStage1=new Stage();
            newStage1.setTitle("登录");
            newStage1.setScene(new Scene(root1, 640, 430));
            newStage1.show();
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("WarnPanel.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage=new Stage();
            newStage.setTitle("注册成功！");
            newStage.setScene(new Scene(root, 640, 430));
            WarnPanelController controller=fxmlLoader.getController();
            controller.setText("注册成功，请登录！");
            newStage.show();
            Stage window = (Stage) submitButton.getScene().getWindow();
            window.close();
            Stage window2 = (Stage) passwordField.getScene().getWindow();
            window2.close();
        }else{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("Warn.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage=new Stage();
            newStage.setTitle("验证码错误！");
            newStage.setScene(new Scene(root, 640, 430));
            WarnPanelController controller=fxmlLoader.getController();
            controller.setText("验证码错误！请重新输入！");
            newStage.show();
        }
    }

    public void exit(MouseEvent mouseEvent) {
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.close();
    }
}
