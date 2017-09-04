package com.akruty.pappu;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

public class App extends Application {
    private double xOffset;
    private double yOffset;
    private VoiceRecoderService voiceRecoderService;
    private CommandProcessor cmdProcessor;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        cmdProcessor = new CommandProcessor();
        cmdProcessor.loadCommands();

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
            SpeechClient speech = null;
            voiceRecoderService.cancel();
            byte[] voiceData = voiceRecoderService.getVoiceData();

            try {
                speech = SpeechClient.create();
            } catch (java.io.IOException ex) {
                System.out.println("Unable to create Google speech client, " +
                        "make sure you have set the environment " +
                        "variable GOOGLE_APPLICATION_CREDENTIALS " + ex.getMessage());
                System.exit(1);
            }

            ByteString audioBytes = ByteString.copyFrom(voiceData);

            System.out.println("Audio byte size " + audioBytes.size());

            // Builds the sync recognize request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speech.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            System.out.println("Size of results " + results.size());

            boolean exit = false;
            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                String command = alternative.getTranscript().trim();

                if (command.equalsIgnoreCase("quit")) {
                    exit = true;
                    break;
                }

                cmdProcessor.runCommand(command);
                System.out.printf("Transcription: %s%n", alternative.getTranscript());
            }

            try {
                speech.close();
                if (exit) {
                    System.exit(1);
                }
            } catch (Exception ex) {
                System.out.println("Problem while closing the speech client");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
