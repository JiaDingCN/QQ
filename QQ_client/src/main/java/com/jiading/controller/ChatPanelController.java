package com.jiading.controller;

import com.jiading.domain.Friend;
import com.jiading.domain.InfoUser;
import com.jiading.utils.PackageList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-19 00:40
 **/
public class ChatPanelController {
    @FXML
    TextArea messagesField;
    @FXML
    TextArea inputField;
    @FXML
    Button submitButton;
    @FXML
    Button exitButton;
    @FXML
    Label chatInfoText;
    @FXML
            Button fileSenderButton;
    Friend toChat;
    String myUsername;
    StringBuilder builder = new StringBuilder();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @FXML
    private void initialize() {
        Listen listen = new Listen();
        listen.start();
    }

    private void insertText(String text, boolean isMe) {
        Date date = new Date();
        builder.append("\n" + format.format(date));
        if (isMe) {
            builder.append("我: " + text + "\n");
        } else {
            builder.append(toChat.getUsername() + ": " + text + "\n");
        }
        messagesField.setText(builder.toString());
    }

    public void setInfo(String myUsername, Friend friend) {
        this.myUsername = myUsername;
        toChat = friend;
        chatInfoText.setText("您和"+toChat.getUsername()+"正在聊天");
    }

    public void submit(MouseEvent mouseEvent) throws UnsupportedEncodingException {
        String message = inputField.getText();
        insertText(message, true);
        inputField.clear();
        InfoUser user = new InfoUser();
        user.setUsername(myUsername);
        user.setToUsername(toChat.getUsername());
        user.setInfoType(InfoUser.InfoTypes.CHAT.toString());
        user.setChatInfo(message);
        PackageList.sendPackage(user);
    }

    public void exit(MouseEvent mouseEvent) {
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.close();
    }

    public void sendFile(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("SendFilePanel.fxml"));
        Parent root = fxmlLoader.load();
        Stage newStage=new Stage();
        newStage.setTitle("发送文件");
        newStage.setScene(new Scene(root, 640, 430));
        SendFilePanelController controller=fxmlLoader.getController();
        controller.setInfo(myUsername,toChat.getUsername());
        newStage.show();
    }

    class Listen extends Thread {
        @Override
        public void run() {
            while (true) {
                if (!PackageList.hasPackagesToReceive()) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    InfoUser receivedUser = PackageList.getReceivedPackage();
                    if (receivedUser.getInfoType().equals(InfoUser.InfoTypes.CHAT.toString())) {
                        insertText(receivedUser.getChatInfo(), false);
                    }
                }
            }
        }
    }
}
