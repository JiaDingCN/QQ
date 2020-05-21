package com.jiading.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.aspectj.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
    File file;
    File folder;

    public void setFile(File file) {
        this.file = file;
    }

    public void chooseFolder(MouseEvent mouseEvent) throws IOException {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("选择要存放文件的文件夹");
        Stage stage = (Stage) receiveButton.getScene().getWindow();
        folder = fileChooser.showDialog(stage);//这个file就是选择的文件夹了
        pathLabel.setText(folder.getPath());
    }

    public void receive(MouseEvent mouseEvent) throws IOException {
        String path=folder.getPath()+File.separator+file.getName();
        File savedFile=new File(path);
        if(savedFile.exists()){
            savedFile.delete();
        }
        savedFile.createNewFile();
        FileUtil.copyFile(file,savedFile);
        file.delete();
        Stage window = (Stage) receiveButton.getScene().getWindow();
        window.close();
    }

    public void exit(MouseEvent mouseEvent) {
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.close();
    }
}
