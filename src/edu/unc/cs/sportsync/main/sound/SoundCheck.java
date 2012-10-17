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
    private TargetDataLine inputLine;
    private SourceDataLine outputLine;
    private boolean isRecording = false;
    private boolean isMuted = false;
    private Mixer inputMixer;
    private Mixer outputMixer;
    private final AudioFormat SOUND_FORMAT;
    private int delayAmount;

    private boolean fullyCached;
    private int bufferCacheCount;
    private int cachingAmount;
    private int maxDelayAmount;

    private byte[] myBuffer;

    private static Settings settings = new Settings();

    /*
     * AudioFormat tells the format in which the data is recorded/played
     */
    public SoundCheck(AudioFormat format, int bufferSize, int theMaxDelayAmount) throws LineUnavailableException {

        /*
         * Get the input/output lines
         */
        BUFFER_SIZE = bufferSize;
        SOUND_FORMAT = format;
        maxDelayAmount = theMaxDelayAmount;
        if (maxDelayAmount == -1) {
            maxDelayAmount = settings.getDelayTime();
        }
        openLines();
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

    protected int calculateRMSLevel(byte[] audioData) {
        long lSum = 0;
        for (int i = 0; i < audioData.length; i++) {
            lSum = lSum + audioData[i];
        }

        double dAvg = lSum / audioData.length;

        double sumMeanSquare = 0d;
        for (int j = 0; j < audioData.length; j++) {
            sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);
        }

        double averageMeanSquare = sumMeanSquare / audioData.length;
        return (int) (Math.pow(averageMeanSquare, 0.5d) + 0.5);
    }

    public int getBufferPercentage() {
        if (fullyCached) {
            return 100;
        }

        double percent = ((double) bufferCacheCount / cachingAmount) * 100;
        return (int) percent;

    }

    public int getInputLevel() {
        return calculateRMSLevel(myBuffer);
    }

    public int getMaxDelay() {
        return maxDelayAmount;
    }

    public double getOutputLevel() {
        return outputLine.getLevel();
    }

    public void openLines() throws LineUnavailableException {
        if (inputLine != null && inputLine.isOpen()) {
            inputLine.close();
        }
        if (outputLine != null && outputLine.isOpen()) {
            outputLine.close();
        }
        inputLine = null;
        outputLine = null;
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
        inputMixer.close();
        outputMixer.close();
        if (inputLine == null || outputLine == null) {
            throw new LineUnavailableException();
        }
        inputLine.open(SOUND_FORMAT, BUFFER_SIZE);
        outputLine.open(SOUND_FORMAT, BUFFER_SIZE);

    }

    @Override
    public void run() {
        myBuffer = new byte[BUFFER_SIZE];
        int delayParam = 170000;
        cachingAmount = (int) (Math.ceil((float) delayParam / BUFFER_SIZE) * maxDelayAmount + 1);
        byte[] outputBufferQueue = new byte[cachingAmount * BUFFER_SIZE];
        bufferCacheCount = 0;
        int offset;
        int delayVar;
        int k;
        fullyCached = false;
        while (isRecording) {
            /*
             * read data from input line
             */

            k = inputLine.read(myBuffer, 0, myBuffer.length);

            // System.out.printf("numbytesread = %d\n", k);
            /*
             * write data to output line
             */

            // outputBufferQueue[count] = myBuffer.clone();
            System.arraycopy(myBuffer, 0, outputBufferQueue, bufferCacheCount * BUFFER_SIZE, BUFFER_SIZE);

            if (bufferCacheCount == cachingAmount - 1) {
                fullyCached = true;
            }
            bufferCacheCount = (bufferCacheCount + 1) % cachingAmount;

            delayVar = (delayAmount * delayParam) / 10;

            if (fullyCached || delayVar < (bufferCacheCount - 1) * BUFFER_SIZE) {
                offset = ((bufferCacheCount + cachingAmount - 1) * BUFFER_SIZE - delayVar) % (cachingAmount * BUFFER_SIZE);

                /*
                 * System.out.printf(
                 * "count = %d offset = %d cachingAmount = %d delayVar = %d bufferSize = %d outputBufferSize = %d\n"
                 * , bufferCacheCount, offset, cachingAmount, delayVar,
                 * BUFFER_SIZE, outputBufferQueue.length);
                 */

                if ((BUFFER_SIZE * cachingAmount - offset) < BUFFER_SIZE) {
                    outputLine.write(outputBufferQueue, offset, (BUFFER_SIZE * cachingAmount - offset));
                    outputLine.write(outputBufferQueue, 0, BUFFER_SIZE - (BUFFER_SIZE * cachingAmount - offset));
                } else {
                    outputLine.write(outputBufferQueue, offset, BUFFER_SIZE);
                }

            }
        }
        inputLine.close();
        outputLine.close();
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
