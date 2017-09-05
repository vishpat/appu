package com.akruty.appu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class App extends Application {
    private double xOffset;
    private double yOffset;
    private VoiceRecoderService voiceRecoderService;
    private SpeechRecognitionService speechRecognitionService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CommandProcessor.loadCommands();

        voiceRecoderService = new VoiceRecoderService();

        primaryStage.initStyle(StageStyle.UNDECORATED);
        Button btn = new Button();
        btn.setText("Oye Pappu");

        btn.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            voiceRecoderService.restart();
        });

        btn.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        btn.setOnMouseReleased(event -> {
            voiceRecoderService.cancel();
            byte[] voiceData = voiceRecoderService.getVoiceData();
            if (voiceData != null) {
                speechRecognitionService = new SpeechRecognitionService(voiceData);
                speechRecognitionService.start();
            }
       });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
