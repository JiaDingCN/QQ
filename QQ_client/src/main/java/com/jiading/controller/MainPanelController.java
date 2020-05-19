package com.jiading.controller;

import com.jiading.domain.Friend;
import com.jiading.domain.InfoUser;
import com.jiading.utils.PackageList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-19 00:36
 **/
public class MainPanelController {
    @FXML
    TableView<Friend> friendsList;
    @FXML
    TableColumn<Friend, String> usernameColumn;
    @FXML
    TableColumn<Friend, String> statusColumn;
    @FXML
    Button exitButton;
    @FXML
            Button addFriendButton;
    @FXML
            Button refreshFriendsListButton;
    String username=null;
    String password=null;
    String friendsString = null;
    Friend chosenFriend;
    Integer chosenIndex;
    ObservableList<Friend> data;

    public void setInfo(String myUsername,String friends,String password) {
        this.password=password;
        this.friendsString = friends;
        this.username=myUsername;
        friendsList.setRowFactory(new Callback<TableView<Friend>, TableRow<Friend>>() {
            @Override
            public TableRow<Friend> call(TableView<Friend> param) {
                return new TableRowController();
            }
        });
        List<Friend> friendsListFromString = Friend.getFriendsListFromString(friendsString);
        data = FXCollections.observableArrayList();
        if(friendsListFromString!=null){
            Iterator<Friend> iterator = friendsListFromString.iterator();
            while (iterator.hasNext()) {
                Friend next = iterator.next();
                data.add(next);
            }
        }
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        friendsList.setItems(data);
    }
    public void renewData(String friends){
        List<Friend> friendsListFromString = Friend.getFriendsListFromString(friends);
        data.clear();
        if(friendsListFromString!=null){
            Iterator<Friend> iterator = friendsListFromString.iterator();
            while (iterator.hasNext()) {
                Friend next = iterator.next();
                data.add(next);
            }
        }
        friendsList.setItems(data);
    }
    public void exit(MouseEvent mouseEvent) {
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.close();
    }

    public void addFriend(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("AddFriendPanel.fxml"));
        Parent root = fxmlLoader.load();
        Stage newStage=new Stage();
        newStage.setTitle("添加好友");
        newStage.setScene(new Scene(root, 640, 430));
        AddFriendPanelController controller=fxmlLoader.getController();
        controller.setUsername(username);
        newStage.show();
    }

    public void refreshFriends(MouseEvent mouseEvent) throws InterruptedException {
        InfoUser user=new InfoUser();
        user.setPassword(password);
        user.setUsername(username);
        user.setInfoType(InfoUser.InfoTypes.SIGNIN.toString());
        PackageList.sendPackage(user);
        while(!PackageList.hasPackagesToReceive()){
            sleep(500);
        }
        InfoUser receivedUser=PackageList.getReceivedPackage();
        renewData(receivedUser.getChatInfo());
    }

    class TableRowController extends TableRow<Friend> {
        public TableRowController(){
            super();
            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton().equals(MouseButton.PRIMARY)
                            && event.getClickCount() == 1
                            && TableRowController.this.getIndex() < friendsList.getItems().size()) {
                        chosenFriend = TableRowController.this.getItem();//获取点击的对象
                        chosenIndex=TableRowController.this.getIndex();//获取点击的index，就是表上的第几项
                    }
                    if(chosenFriend.getStatus().equals("离线")){
                        FXMLLoader fxmlLoader2=new FXMLLoader(getClass().getClassLoader().getResource("WarnPanel.fxml"));
                        Parent root2 = null;
                        try {
                            root2 = fxmlLoader2.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Stage newStage2=new Stage();
                        newStage2.setTitle("该用户不在线！");
                        newStage2.setScene(new Scene(root2, 640, 430));
                        WarnPanelController controller2=fxmlLoader2.getController();
                        controller2.setText("该用户不在线，不能聊天！");
                        newStage2.show();
                    }else{
                        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("ChatPanel.fxml"));
                        Parent root = null;
                        try {
                            root = fxmlLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Stage newStage=new Stage();
                        newStage.setTitle("聊天");
                        newStage.setScene(new Scene(root, 640, 600));
                        ChatPanelController controller=fxmlLoader.getController();
                        controller.setInfo(username,chosenFriend);
                        newStage.show();
                    }

                }
            });
        }
    }
}
