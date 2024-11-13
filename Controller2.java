package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;


public class Controller2 {
    @FXML
    private Button AddButton;
    @FXML
    private ComboBox playlistCombo,songsBox;
    @FXML
    private TextArea nameArea;

    private int index=0;
    private String[] songs=new String[3];
    public void fillPlaylist(String[] tracks) {
        for(int i=0;i<tracks.length;i++) {
            songsBox.getItems().add(tracks[i]);
            songs[i]=tracks[i];
        }
    }
    public void addSong() {
        String song= (String) songsBox.getValue();
        playlistCombo.getItems().add(song);
        if(index<songs.length){
            songs[index]=song;
            index++;
        }
    }
    public void saveAndReturn(ActionEvent event) throws IOException {
        String name= nameArea.getText();


        FXMLLoader loader=new FXMLLoader(getClass().getResource("hello-view.fxml"));
        root=loader.load();

        Controller controller=loader.getController();
        controller.makeNewPlaylist(name,songs);


        //root= FXMLLoader.load(getClass().getResource("scene2.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    private Parent root;
    private Stage stage;
    private Scene scene;
    public void returntoscene(ActionEvent event) throws IOException {
        root= FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
