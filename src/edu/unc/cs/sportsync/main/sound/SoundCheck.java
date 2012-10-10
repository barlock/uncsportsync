package edu.unc.cs.sportsync.main.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import edu.unc.cs.sportsync.main.settings.Settings;

public class SoundCheck extends Thread {
    private final int BUFFER_SIZE;
    private TargetDataLine inputLine = null;
    private SourceDataLine outputLine = null;
    private boolean isRecording = false;
    private boolean isMuted = false;
    private final Mixer inputMixer;
    private final Mixer outputMixer;
    private final AudioFormat SOUND_FORMAT;
    private int delayAmount;

    private static final Settings settings = new Settings();

    /*
     * AudioFormat tells the format in which the data is recorded/played
     */
    public SoundCheck(AudioFormat format, int bufferSize) throws LineUnavailableException {

        /*
         * Get the input/output lines
         */
        BUFFER_SIZE = bufferSize;
        SOUND_FORMAT = format;
        inputMixer = AudioSystem.getMixer(settings.getInputMixer());
        outputMixer = AudioSystem.getMixer(settings.getOutputMixer());
        inputMixer.open();
        outputMixer.open();
        Line.Info[] targetlineinfos = inputMixer.getTargetLineInfo();
        for (int i = 0; i < targetlineinfos.length; i++) {
            if (targetlineinfos[i].getLineClass() == TargetDataLine.class) {
                inputLine = (TargetDataLine) AudioSystem.getLine(targetlineinfos[i]);
                break;
            }
        }
        Line.Info[] sourcelineinfos = outputMixer.getSourceLineInfo();
        for (int i = 0; i < sourcelineinfos.length; i++) {
            if (sourcelineinfos[i].getLineClass() == SourceDataLine.class) {
                outputLine = (SourceDataLine) AudioSystem.getLine(sourcelineinfos[i]);
                break;
            }
        }
        if (inputLine == null || outputLine == null) {
            throw new LineUnavailableException();
        }

        /*
         * inputLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
         * outputLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
         * DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class,
         * format, bufferSize); DataLine.Info sourceInfo = new
         * DataLine.Info(SourceDataLine.class, format, bufferSize);
         */

        /*
         * Open the input/output lines
         */
        inputLine.open(SOUND_FORMAT, BUFFER_SIZE);
        outputLine.open(SOUND_FORMAT, BUFFER_SIZE);
    }

    @Override
    public void run() {
        byte[] myBuffer = new byte[BUFFER_SIZE];
        int delayParam = 170000;
        int cachingAmount = (int) (Math.ceil((float) delayParam / BUFFER_SIZE) * settings.getDelayTime() + 1);
        byte[] outputBufferQueue = new byte[cachingAmount * BUFFER_SIZE];
        int count = 0, offset;
        int delayVar;
        boolean fullyCached = false;
        while (isRecording) {
            /*
             * read data from input line
             */

            inputLine.read(myBuffer, 0, myBuffer.length);

            /*
             * write data to output line
             */

            // outputBufferQueue[count] = myBuffer.clone();
            System.arraycopy(myBuffer, 0, outputBufferQueue, count * BUFFER_SIZE, BUFFER_SIZE);

            if (count == cachingAmount - 1) {
                fullyCached = true;
            }
            count = (count + 1) % cachingAmount;

            delayVar = (delayAmount * delayParam) / 10;

            if (fullyCached || delayVar < (count - 1) * BUFFER_SIZE) {
                offset = ((count + cachingAmount - 1) * BUFFER_SIZE - delayVar) % (cachingAmount * BUFFER_SIZE);
                // System.out.printf("count = %d offset = %d cachingAmount = %d delayVar = %d bufferSize = %d outputBufferSize = %d\n",
                // count, offset, cachingAmount, delayVar, BUFFER_SIZE,
                // outputBufferQueue.length);
                if ((BUFFER_SIZE * cachingAmount - offset) < BUFFER_SIZE) {
                    outputLine.write(outputBufferQueue, offset, (BUFFER_SIZE * cachingAmount - offset));
                    outputLine.write(outputBufferQueue, 0, BUFFER_SIZE - (BUFFER_SIZE * cachingAmount - offset));
                } else {
                    outputLine.write(outputBufferQueue, offset, BUFFER_SIZE);
                }

            }
        }
    }

    public void setDelayAmount(int amount) {
        delayAmount = amount;
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

}
