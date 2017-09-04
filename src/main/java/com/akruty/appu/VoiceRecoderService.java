package com.akruty.appu;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.sound.sampled.*;

import java.io.ByteArrayOutputStream;

/**
 * Created by vpati011 on 9/3/17.
 */
public class VoiceRecoderService extends Service<byte[]> {

    private int counter = 1;

    private TargetDataLine microphone;

    private ByteArrayOutputStream voiceOutput;

    public VoiceRecoderService() {
        counter = 1;

        AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Unable to access microphone");
            System.exit(1);
        }

        try {
            microphone = AudioSystem.getTargetDataLine(format);
            microphone.open(format);
        } catch (LineUnavailableException e) {
            System.err.println("Unable to access microphone");
            System.exit(1);
        }
    }

    public int getCounter() {
        return counter;
    }

    public byte[] getVoiceData() {
        return voiceOutput.toByteArray();
    }

    @Override
    protected Task<byte[]> createTask() {
        return new Task<byte[]>() {

            @Override
            protected byte[] call() throws Exception {
                counter = 0;
                voiceOutput = new ByteArrayOutputStream();
                int numBytesRead;
                byte data[] = new byte[microphone.getBufferSize() / 5];
                microphone.start();
                while (!isCancelled()) {
                    numBytesRead = microphone.read(data, 0, data.length);
                    voiceOutput.write(data, 0, numBytesRead);
                    counter++;
                }
                microphone.stop();
                return voiceOutput.toByteArray();
            }
        };
    }
}
