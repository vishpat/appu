package com.akruty.appu;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.util.List;

/**
 * Created by vpati011 on 9/4/17.
 */
public class SpeechRecognitionService extends Service<Void> {

    private byte[] voiceData;

    public SpeechRecognitionService(byte[] voiceData) {
        this.voiceData = voiceData;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                SpeechClient speech = null;
                CommandProcessor cmdProcessor = new CommandProcessor();

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

                return null;
            }
        };
    }
}
