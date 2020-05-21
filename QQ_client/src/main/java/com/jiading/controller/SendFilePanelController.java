package com.jiading.controller;

import com.jiading.domain.InfoUser;
import com.jiading.service.SocketService;
import com.jiading.utils.PackageList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-20 10:51
 **/
public class SendFilePanelController {
    @FXML
    Label titleLabel;
    @FXML
    Label pathLabel;
    @FXML
    Button chooseFile;
    @FXML
    Button sendButton;
    @FXML
    Button exitButton;
    Socket socket;
    File fileToSend;
    String username;
    String toUsername;

    public void setInfo(String username, String toUsername) {
        this.username = username;
        this.toUsername = toUsername;
        this.socket = SocketService.getSocket();
        titleLabel.setText("向" + toUsername + "发送文件:");
    }

    @FXML
    public void initialize() {
        titleLabel.setText("向" + toUsername + "发送文件:");
    }

    public void chooseFile(MouseEvent mouseEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("选择要发送的文件");
        Stage stage = (Stage) sendButton.getScene().getWindow();
        fileToSend = chooser.showOpenDialog(stage);
        pathLabel.setText(fileToSend.getPath());
    }

    public void send(MouseEvent mouseEvent) throws IOException, InterruptedException {
        PackageList.setIsInTransferFile(true);
        FileInputStream fis = new FileInputStream(fileToSend);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        InfoUser infoUser = new InfoUser();
        infoUser.setUsername(username);
        infoUser.setToUsername(toUsername);
        infoUser.setCode(fileToSend.getName());
        infoUser.setInfoType(InfoUser.InfoTypes.SENDFILE.toString());
        long times = fileToSend.length();
        if (fileToSend.length() % 1024 != 0) {
            times++;
        }
        infoUser.setChatInfo(String.valueOf(fileToSend.length()));
        PackageList.sendPackage(infoUser);
        while (PackageList.hasPackagesToSend()) {
            sleep(500);
        }
        byte[] buf = new byte[1024];
        int length = 0;
        int count = 0;
        while (count < times) {
            length = fis.read(buf, 0, buf.length);
            if (length == -1) {
                break;
            }
            //System.out.println(length);
            count += length;
            dos.write(buf, 0, length);
            dos.flush();
        }
        fis.close();
        Stage window = (Stage) sendButton.getScene().getWindow();
        window.close();
        PackageList.setIsInTransferFile(false);
    }

    public void exit(MouseEvent mouseEvent) {
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.close();
    }
}
