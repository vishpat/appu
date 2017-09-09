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


        primaryStage.initStyle(StageStyle.UNDECORATED);

        Button btn = new Button();
        btn.setText("Appu");
        btn.setId("button-style");

        btn.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            voiceRecoderService = new VoiceRecoderService();
            voiceRecoderService.start();
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
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/appu.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
