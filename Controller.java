package com.example.demo;

import java.io.*;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Label songlabel;
    @FXML
    private Label volumeValue;
    @FXML
    private Button nextButton,previousButton,MakePlaylistButton,AboutButton;
    @FXML
    private Slider playbackSlider,volumeSlider;
    @FXML
    private ImageView songcover;
    @FXML
    private ImageView pausePic;
    @FXML
    private ToggleButton pauseButton;
    @FXML
    private ComboBox queueBox;
    @FXML
    private Label songNameLabel;
    @FXML
    private Label playlistLabel;

    Image pause=new Image(getClass().getResourceAsStream("/assets/pause.png"));
    Image play=new Image(getClass().getResourceAsStream("/assets/play.png"));

    //Playlist
    private Track[] tracks= new Track[3];
    //Flag resuming/stopping mediaplayer
    private boolean running=false;
    //File variables
    private ArrayList<File> songs;

    private int songNumber=0;
    private Track currentTrack;
    private Media media;
    private MediaPlayer mediaPlayer;
    //Images
    Image HellHoleImage=new Image(getClass().getResourceAsStream("/assets/Six_Desperate_Ballads.png"));
    Image VulgarDisplayofPowerImage=new Image(getClass().getResourceAsStream("/assets/Vulgar_Display_of_Power.png"));
    Image NightTimeImage=new Image(getClass().getResourceAsStream("/assets/NightTime.png"));

    //Timer
    private Timer timer;
    private TimerTask timerTask;

    //Scene switcher
    File HellHoleFile = new File("src/main/resources/assets/music/The_Garden-Hell_Hole.wav");
    File ThisLoveFile = new File("src/main/resources/assets/music/This_Love.wav");
    File EightiesFile = new File("src/main/resources/assets/music/Eighties.wav");
    Track HellHoleTrack=new Track("Hell Hole","The Garden","Six Desperate Ballads","EP",5,"2:03",HellHoleFile, "assets/music/The_Garden-Hell_Hole.wav",2024,HellHoleImage);
    Track ThisLoveTrack=new Track("This Love","Pantera","Vulgar Display of Power","Album",5,"6:33",ThisLoveFile, "assets/music/This_Love.wav",1992,VulgarDisplayofPowerImage);
    Track EightiesTrack=new Track("Eighties - 2007 Digital Remaster","Killing Joke","Nighttime","Album",8,"3:59",EightiesFile, "assets/music/Eighties.wav",1985,NightTimeImage);


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tracks[0]=HellHoleTrack;
        tracks[1]=ThisLoveTrack;
        tracks[2]=EightiesTrack;

        songs =new ArrayList<File>();

        songs.add(HellHoleFile);
        songs.add(ThisLoveFile);
        songs.add(EightiesFile);




        media =new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songNameLabel.setText(tracks[0].getArtist()+" - "+tracks[0].getTrackName());

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
                volumeValue.setText((int) volumeSlider.getValue()+"%");
            }
        });

        System.out.println(Duration.millis(media.getDuration().toSeconds()));


    }
    public void playMedia(){
        if(pauseButton.isSelected()){
            running=true;
            playSong();
        }else{
            running=false;
            playSong();
        }
    }
    public void nextMedia(){
        songNumber=songNumber+1;
        if(songNumber==0){
            currentTrack=tracks[songNumber];
        }else if(songNumber==tracks.length){
            songNumber=0;
            currentTrack=tracks[songNumber];
        }else{
            currentTrack=tracks[songNumber];
        }
        //GUI Changes:
        songcover.setImage(currentTrack.getSongcover());
        songNameLabel.setText(currentTrack.getArtist()+" - "+currentTrack.getTrackName());
        //File change
        mediaPlayer.stop();
        media =new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        playSong();
    }
    public void previousMedia(){
        songNumber=songNumber-1;
        if(songNumber==0){
            currentTrack=tracks[songNumber];
        }else if(songNumber==-1){
            songNumber= tracks.length-1;
            System.out.println(tracks.length-1);
            currentTrack=tracks[songNumber];
        }else{
            currentTrack=tracks[songNumber];
        }
        //GUI Changes:
        songcover.setImage(currentTrack.getSongcover());
        songNameLabel.setText(currentTrack.getArtist()+" - "+currentTrack.getTrackName());
        //File changes
        mediaPlayer.stop();
        media =new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        playSong();
    }
    private void playSong(){
        if(running){
            beginTimer();
            mediaPlayer.play();
            pausePic.setImage(pause);
        }else{
            cancelTimer();
            mediaPlayer.pause();
            pausePic.setImage(play);
        }
    }
    public void beginTimer(){
        timer =new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                double current=mediaPlayer.getCurrentTime().toSeconds();
                double end=media.getDuration().toSeconds();
                System.out.println(current/end);

                playbackSlider.setValue( (current/end)*100 );

                if(current/end==1){
                    cancelTimer();
                }

            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }
    public void cancelTimer(){
        timer.cancel();
    }
    public void updateSlider(){
        mediaPlayer.seek(Duration.millis(media.getDuration().toSeconds()));
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene2(ActionEvent event) throws IOException {
        String tracknames[]= new String[tracks.length];
        for(int i=0;i<tracks.length;i++){
            tracknames[i]=tracks[i].getArtist()+" - "+tracks[i].getTrackName();
        }

        FXMLLoader loader=new FXMLLoader(getClass().getResource("scene2.fxml"));
        root=loader.load();

        Controller2 controller2=loader.getController();
        controller2.fillPlaylist(tracknames);


        //root= FXMLLoader.load(getClass().getResource("scene2.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void makeNewPlaylist(String name,String[] playlist){
        System.out.println(name);
        changeGui(name,playlist);
    }

    public void changeGui(String name,String[] playlist){
        queueBox.getItems().clear();
        songNumber=0;
        songs.clear();
        for(int i=0;i<playlist.length;i++){
            System.out.println(playlist[i]);
            if(playlist[i].equals("The Garden - Hell Hole")){
                songs.add(HellHoleFile);
                tracks[i]=HellHoleTrack;
            }else if(playlist[i].equals("Pantera - This Love")){
                songs.add(ThisLoveFile);
                tracks[i]=ThisLoveTrack;
            }else{
                tracks[i]=EightiesTrack;
                songs.add(EightiesFile);
            }
        }
        currentTrack=tracks[songNumber];
        media =new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        for(int i=0;i<playlist.length;i++){
            queueBox.getItems().add(playlist[i]);
        }

        songcover.setImage(currentTrack.getSongcover());
        songNameLabel.setText(currentTrack.getArtist()+" - "+currentTrack.getTrackName());
        playlistLabel.setText(name);
    }

    public void switchToScene3(ActionEvent event) throws IOException {
        root= FXMLLoader.load(getClass().getResource("scene3.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
