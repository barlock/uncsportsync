package edu.unc.cs.sportsync.main.sound;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.EnumControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.ui.error.ErrorUtil;

public class AudioControl {

	private static ArrayList<Mixer.Info> targetMixers = new ArrayList<Mixer.Info>();

	private static ArrayList<Mixer.Info> sourceMixers = new ArrayList<Mixer.Info>();

	private Settings settings;

	private boolean isMuted;
	private boolean isPlayingTestAudio;

	private SoundCheck mySoundCheck;

	private final LineListener testAudioListener = new LineListener() {
		@Override
		public void update(LineEvent event) {
			if (event.getType() == LineEvent.Type.STOP) {
				isPlayingTestAudio = false;
			}
		}
	};

	public AudioControl() {
		isMuted = false;
		isPlayingTestAudio = false;
		settings = null;
	}

	public void closeLines() {
		mySoundCheck.dispose();
	}

	// public void setMaxDelay(int maxDelay) {
	// settings.setDelayTime(maxDelay);
	//
	// if (maxDelay != mySoundCheck.getMaxDelay()) {
	// System.out.println(mySoundCheck.getMaxDelay());
	// mySoundCheck.resetBuffer();
	// }
	//
	// mySoundCheck.updateMaxDelay();
	// }

	public int getBufferPercentage() {
		if (mySoundCheck != null) {
			return mySoundCheck.getBufferPercentage();
		} else {
			return 0;
		}
	}

	public int getInputLevel() {
		return mySoundCheck.getInputLevel();
	}

	public double getOutputLevel() {
		return mySoundCheck.getOutputLevel();
	}

	public boolean isMuted() {
		return isMuted;
	}

	public void prepareMixerList() {
		targetMixers = getInputDevices();
		sourceMixers = getOutputDevices();
		if (mySoundCheck != null) {
			try {
				mySoundCheck.openLines();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				ErrorUtil.openStackTraceDialog("A Fatal Error has occured and the application will need to shut down", e);
				System.exit(1);
			}
		}
	}

	public void resetBuffer() {
		mySoundCheck.resetBuffer();
	}

	public void setDelayAmount(int delayAmount) {
		mySoundCheck.setDelayAmount(delayAmount);
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public void setVolume(double percentLevel) {
		mySoundCheck.setVolume(percentLevel);
	}

	public void start() {
		float frameRate = (float) 44100.0;
		int BUFFER_SIZE = 40960;
		AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, frameRate, 16, 2, 4, frameRate, false);
		mySoundCheck = null;
		try {
			mySoundCheck = new SoundCheck(audioFormat, BUFFER_SIZE, settings, testAudioListener);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			ErrorUtil.openStackTraceDialog("A Fatal Error has occured and the application will need to shut down", e);
			System.exit(1);
		}
		mySoundCheck.start();

	}

	public void stopTestOutput() {
		if (isPlayingTestAudio) {
			mySoundCheck.stopTestOutput();
			isPlayingTestAudio = false;
		}
	}

	public void startTestOutput() {
		if (!isPlayingTestAudio) {
			mySoundCheck.playTestOutput();
		} else {
			mySoundCheck.stopTestOutput();
		}
		isPlayingTestAudio = !isPlayingTestAudio;
	}

	public void toggleMute() {
		isMuted = !isMuted;

		mySoundCheck.toggleMute();
	}

	public void updateLines() {
		try {
			mySoundCheck.openLines();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			ErrorUtil.openStackTraceDialog("A Fatal Error has occured and the application will need to shut down", e);
			System.exit(1);
		}
		mySoundCheck.setVolume();
		if (isMuted) {
			mySoundCheck.toggleMute();
		}
	}

	@SuppressWarnings("unused")
	private static String AnalyzeControl(Control thisControl) {
		String type = thisControl.getType().toString();

		if (thisControl instanceof BooleanControl) {
			return "\tControl: " + type + " (boolean)";
		}

		if (thisControl instanceof CompoundControl) {
			System.out.println("\tControl: " + type + " (compound - values below)");
			String toReturn = "";
			for (Control children : ((CompoundControl) thisControl).getMemberControls()) {
				toReturn += "  " + AnalyzeControl(children) + "\n";
			}
			return toReturn.substring(0, toReturn.length() - 1);
		}

		if (thisControl instanceof EnumControl) {
			return "\tControl:" + type + " (enum: " + thisControl.toString() + ")";
		}

		if (thisControl instanceof FloatControl) {
			return "\tControl: " + type + " (float: from " + ((FloatControl) thisControl).getMinimum() + " to " + ((FloatControl) thisControl).getMaximum() + ")";
		}
		return "\tControl: unknown type";
	}

	public static ArrayList<Mixer.Info> getInputDevices() {
		ArrayList<Mixer.Info> targetMixers = new ArrayList<Mixer.Info>();

		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

		for (int i = 0; i < mixerInfos.length; i++) {
			Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);
			try {
				mixer.open();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				ErrorUtil.openStackTraceDialog("A Fatal Error has occured and the application will need to shut down", e);
				System.exit(1);
			}
			Line.Info[] targetLines = mixer.getTargetLineInfo();
			for (Line.Info info : targetLines) {
				if (info.getLineClass() == TargetDataLine.class) {
					targetMixers.add(mixerInfos[i]);
				}
			}
			mixer.close();
		}

		return targetMixers;
	}

	public static ArrayList<Mixer.Info> getOutputDevices() {
		ArrayList<Mixer.Info> sourceMixers = new ArrayList<Mixer.Info>();

		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

		for (int i = 0; i < mixerInfos.length; i++) {
			Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);
			try {
				mixer.open();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				ErrorUtil.openStackTraceDialog("A Fatal Error has occured and the application will need to shut down", e);
				System.exit(1);
			}
			Line.Info[] sourceLines = mixer.getSourceLineInfo();
			for (Line.Info info : sourceLines) {
				if (info.getLineClass() == SourceDataLine.class) {
					sourceMixers.add(mixerInfos[i]);
				}
			}
			mixer.close();
		}

		return sourceMixers;
	}

	public static ArrayList<Mixer.Info> getSourceMixers() {
		return sourceMixers;
	}

	public static ArrayList<Mixer.Info> getTargetMixers() {
		return targetMixers;
	}
}
