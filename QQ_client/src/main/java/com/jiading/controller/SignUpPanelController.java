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
 * @create: 2020-05-19 00:04
 **/
public class SignUpPanelController {
    @FXML
    Button submitButton;
    @FXML
    Button exitButton;
    @FXML
    PasswordField passwordField;
    @FXML
    TextField usernameField;
    @FXML
    TextField emailField;

    public void submit(MouseEvent mouseEvent) throws InterruptedException, IOException {
        InfoUser user = new InfoUser();
        user.setUsername(usernameField.getText());
        user.setPassword(passwordField.getText());
        user.setEmail(emailField.getText());
        user.setInfoType(InfoUser.InfoTypes.SIGNUP.toString());
        user.setChatInfo(InfoUser.stateCodes.ASKCODE.toString());
        PackageList.sendPackage(user);
        while (!PackageList.hasPackagesToReceive()) {
            sleep(500);
        }
        InfoUser receivedUser = PackageList.getReceivedPackage();
        if (receivedUser.getChatInfo().equals(InfoUser.stateCodes.CODEASKED.toString())) {
            //接受到了验证码
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("SignUpPanelCode.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage=new Stage();
            newStage.setTitle("请输入验证码");
            newStage.setScene(new Scene(root, 640, 430));
            SignUpPanelCodeController controller=fxmlLoader.getController();
            controller.setInfoUser(user);
            controller.setPasswordField(passwordField);
            newStage.show();
        }else{
            //注册错误
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("WarnPanel.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage=new Stage();
            newStage.setTitle("用户名已存在");
            newStage.setScene(new Scene(root, 640, 430));
            WarnPanelController controller = fxmlLoader.getController();
            controller.setText("该用户名已被注册，请重新选择用户名！");
            newStage.show();
        }
    }


    public void exit(MouseEvent mouseEvent) {
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.close();
    }
}
