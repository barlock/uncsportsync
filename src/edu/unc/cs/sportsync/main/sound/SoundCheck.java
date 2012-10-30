package edu.unc.cs.sportsync.main.sound;

import java.io.BufferedInputStream;
import java.text.DecimalFormat;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.ui.error.ErrorUtil;

public class SoundCheck extends Thread {
	private final int DELAY_PARAM = 170000;

	private final int BUFFER_SIZE;
	private TargetDataLine inputLine;
	private SourceDataLine outputLine;
	private Mixer inputMixer;
	private Mixer outputMixer;
	private final AudioFormat SOUND_FORMAT;
	private int delayAmount;

	private boolean fullyCached;
	private int bufferCacheCount;
	private int cachingAmount;
	private int maxDelayAmount;

	private byte[] myBuffer;
	private byte[] outputBufferQueue;

	private boolean disposed;
	private final Settings settings;

	private double percentLevelVolume;

	private Clip myClip = null;
	private final LineListener testAudioListener;

	/*
	 * AudioFormat tells the format in which the data is recorded/played
	 */
	public SoundCheck(AudioFormat format, int bufferSize, Settings settings, LineListener tAudioListener) throws LineUnavailableException {
		this.settings = settings;
		/*
		 * Get the input/output lines
		 */
		BUFFER_SIZE = bufferSize;
		SOUND_FORMAT = format;
		maxDelayAmount = settings.getMaxDelay();
		disposed = false;
		testAudioListener = tAudioListener;

		openLines();
	}

	protected int calculateRMSLevel(byte[] chunk) {
		DecimalFormat df = new DecimalFormat("##");
		float audioVal = 0;
		for (int i = 0; i < chunk.length - 1; i++) {
			audioVal = (float) (((chunk[i + 1] << 8) | (chunk[i] & 0xff)) / 32768.0);
		}

		int percent = Integer.valueOf(df.format(Math.abs(audioVal) * 1000));
		// System.out.println(percent);

		return percent;
	}

	public synchronized void dispose() {
		inputLine.close();
		outputLine.close();
		disposed = true;
	}

	public int getBufferPercentage() {
		double percent = fullyCached ? 100 : ((double) bufferCacheCount / cachingAmount) * 100;
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

	public synchronized void openLines() throws LineUnavailableException {
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

		inputLine.start();
		outputLine.start();
	}

	public void playTestOutput() {
		AudioInputStream testFileInputStream = null;
		Mixer.Info mixer = settings.getOutputMixer();

		try {
			testFileInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(this.getClass().getResourceAsStream("UNCFightSong.wav")));
			myClip = AudioSystem.getClip(mixer);
			myClip.open(testFileInputStream);
		} catch (Exception e) {
			e.printStackTrace();
			ErrorUtil.openStackTraceDialog("A Fatal Error has occured and the application will need to shut down", e);
			System.exit(1);
		}

		myClip.addLineListener(testAudioListener);
		myClip.start();
		setVolume();
	}

	private synchronized int read(byte[] buffer, int offset, int length) {
		return inputLine.read(buffer, offset, length);
	}

	public synchronized void resetBuffer() {
		cachingAmount = (int) (Math.ceil((float) DELAY_PARAM / BUFFER_SIZE) * maxDelayAmount + 1);
		outputBufferQueue = new byte[cachingAmount * BUFFER_SIZE];
		bufferCacheCount = 0;
		fullyCached = false;
		delayAmount = 0;
	}

	@Override
	public void run() {
		int offset;
		int delayVar;

		myBuffer = new byte[BUFFER_SIZE];
		resetBuffer();

		while (!disposed) {
			// read data from input line
			read(myBuffer, 0, myBuffer.length);

			// System.out.printf("numbytesread = %d\n", k);

			// write to output line
			// outputBufferQueue[count] = myBuffer.clone();
			System.arraycopy(myBuffer, 0, outputBufferQueue, bufferCacheCount * BUFFER_SIZE, BUFFER_SIZE);

			if (bufferCacheCount == cachingAmount - 1) {
				fullyCached = true;
			}

			bufferCacheCount = (bufferCacheCount + 1) % cachingAmount;

			delayVar = (delayAmount * DELAY_PARAM) / 10;

			if (fullyCached || delayVar < (bufferCacheCount - 1) * BUFFER_SIZE) {
				offset = ((bufferCacheCount + cachingAmount - 1) * BUFFER_SIZE - delayVar) % (cachingAmount * BUFFER_SIZE);

				// System.out.printf("count = %d offset = %d cachingAmount = %d delayVar = %d bufferSize = %d outputBufferSize = %d\n",
				// bufferCacheCount, offset, cachingAmount, delayVar,
				// BUFFER_SIZE,
				// outputBufferQueue.length);

				if ((BUFFER_SIZE * cachingAmount - offset) < BUFFER_SIZE) {
					write(outputBufferQueue, offset, (BUFFER_SIZE * cachingAmount - offset));
					write(outputBufferQueue, 0, BUFFER_SIZE - (BUFFER_SIZE * cachingAmount - offset));
				} else {
					write(outputBufferQueue, offset, BUFFER_SIZE);
				}

			}
		}
	}

	public void setDelayAmount(int amount) {
		delayAmount = amount;
	}

	public void setVolume() {
		if (outputLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl volume = (FloatControl) outputLine.getControl(FloatControl.Type.MASTER_GAIN);
			float minimum = volume.getMinimum();
			float maximum = volume.getMaximum();
			float currentVolume = (float) (minimum + percentLevelVolume * (maximum - minimum) / 100.0F);
			volume.setValue(currentVolume);
		}
		if (myClip != null && myClip.isOpen() && myClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl volume = (FloatControl) myClip.getControl(FloatControl.Type.MASTER_GAIN);
			float minimum = volume.getMinimum();
			float maximum = volume.getMaximum();
			float currentVolume = (float) (minimum + percentLevelVolume * (maximum - minimum) / 100.0F);
			volume.setValue(currentVolume);
		}
	}

	public void setVolume(double percentLevel) {
		if (outputLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			percentLevelVolume = percentLevel;
			FloatControl volume = (FloatControl) outputLine.getControl(FloatControl.Type.MASTER_GAIN);
			float minimum = volume.getMinimum();
			float maximum = volume.getMaximum();
			float currentVolume = (float) (minimum + percentLevelVolume * (maximum - minimum) / 100.0F);
			volume.setValue(currentVolume);
		}
		if (myClip != null && myClip.isOpen() && myClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl volume = (FloatControl) myClip.getControl(FloatControl.Type.MASTER_GAIN);
			float minimum = volume.getMinimum();
			float maximum = volume.getMaximum();
			float currentVolume = (float) (minimum + percentLevelVolume * (maximum - minimum) / 100.0F);
			volume.setValue(currentVolume);
		}
	}

	@Override
	public void start() {
		super.start();
	}

	public synchronized void stopTestOutput() {
		myClip.stop();
	}

	public synchronized void toggleMute() {
		BooleanControl bc = (BooleanControl) outputLine.getControl(BooleanControl.Type.MUTE);
		if (bc != null) {
			bc.setValue(!bc.getValue());
		}
	}

	public void updateMaxDelay() {
		maxDelayAmount = settings.getMaxDelay();
	}

	// possibly synchronized?
	public void write(byte[] buffer, int offset, int length) {
		outputLine.write(buffer, offset, length);
	}
}
