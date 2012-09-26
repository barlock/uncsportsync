package edu.unc.cs.sportsync.main.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class SoundCheck extends Thread {
    private final int BUFFER_SIZE;
    private final TargetDataLine inputLine;
    private final SourceDataLine outputLine;
    private boolean isRecording = false;
    private boolean isMuted = false;

    /*
     * AudioFormat tells the format in which the data is recorded/played
     */
    public SoundCheck(AudioFormat format, int bufferSize) throws LineUnavailableException {

        /*
         * Get the input/output lines
         */
        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format, bufferSize);
        DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format, bufferSize);
        BUFFER_SIZE = bufferSize;
        inputLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
        outputLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
        /*
         * Open the input/output lines
         */
        inputLine.open(format, bufferSize);
        outputLine.open(format, bufferSize);
    }

    @Override
    public void run() {
        byte[] myBuffer = new byte[BUFFER_SIZE];
        while (isRecording) {
            /*
             * read data from input line
             */
            int numBytesCaptured = inputLine.read(myBuffer, 0, myBuffer.length);

            /*
             * write data to output line
             */
            outputLine.write(myBuffer, 0, numBytesCaptured);
        }
    }

    public void setVolume(double percentLevel) {
        if (outputLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volume = (FloatControl) outputLine.getControl(FloatControl.Type.MASTER_GAIN);
            float minimum = volume.getMinimum();
            float maximum = volume.getMaximum();
            float newValue = (float) (minimum + percentLevel * (maximum - minimum) / 100.0F);
            volume.setValue(newValue);
        }
    }

    @Override
    public void start() {
        inputLine.start();
        outputLine.start();
        isRecording = true;
        super.start();
    }

    public void stopRecording() {
        isRecording = false;
    }

    public void toggleMute() {
        isMuted = !isMuted;
        BooleanControl bc = (BooleanControl) outputLine.getControl(BooleanControl.Type.MUTE);
        if (bc != null) {
            bc.setValue(isMuted);
        }
    }

    // public static void main(String[] args)
    // {
    // float frameRate = (float) 44100.0;
    //
    // AudioFormat audioFormat = new
    // AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
    // frameRate, 16, 2, 4, frameRate, false);
    // SoundCheck myAudioLoop = null;
    // try
    // {
    // myAudioLoop = new SoundCheck(audioFormat,
    // 40960);
    // }
    // catch (LineUnavailableException e)
    // {
    // System.exit(1);
    // }
    // myAudioLoop.start();
    // }
}
