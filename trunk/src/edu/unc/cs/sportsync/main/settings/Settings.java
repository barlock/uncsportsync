package edu.unc.cs.sportsync.main.settings;

import java.io.File;

import javax.sound.sampled.Mixer;

import edu.unc.cs.sportsync.main.sound.AudioControl;

public class Settings {
	private final String PATH = "settings.xml";
	private int maxDelay;
	private int volume;

	private Mixer.Info inputMixer;
	private Mixer.Info outputMixer;

	public Settings() {
		File file = new File(PATH);
		boolean failed = false;

		// Init settings to default
		setDefaultSettings();

		if (file.exists()) {
			failed = !parseSettingsFile(file);
		}
	}

	public int getMaxDelay() {
		return maxDelay;
	}

	public Mixer.Info getInputMixer() {
		return inputMixer;
	}

	public Mixer.Info getOutputMixer() {
		return outputMixer;
	}

	public int getVolume() {
		return volume;
	}

	private boolean parseSettingsFile(File file) {
		try {
			SettingsXmlParser parser = new SettingsXmlParser(file);
			if (parser.getDelayTime() != null) {
				maxDelay = Integer.valueOf(parser.getDelayTime());
			}
			if (parser.getVolume() != null) {
				volume = Integer.valueOf(parser.getVolume());
			}
			if (parser.getInputMixer() != null) {
				inputMixer = parser.getInputMixer();
			}
			if (parser.getOutputMixer() != null) {
				outputMixer = parser.getOutputMixer();
			}
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}

		return true;
	}

	public void save() {
		SettingsXmlBuilder builder = new SettingsXmlBuilder();

		builder.addDelayTime(maxDelay);
		builder.addVolume(volume);
		builder.addInputMixer(inputMixer);
		builder.addOutputMixer(outputMixer);

		builder.saveTo(PATH);
	}

	private void setDefaultSettings() {
		maxDelay = 60;
		volume = 50;
		inputMixer = AudioControl.getTargetMixers().iterator().next();
		outputMixer = AudioControl.getSourceMixers().iterator().next();
	}

	public void setMaxDelay(int maxDelay) {
		this.maxDelay = maxDelay;
	}

	public void setInputMixer(Mixer.Info inputMixer) {
		this.inputMixer = inputMixer;
	}

	public void setOutputMixer(Mixer.Info outputMixer) {
		this.outputMixer = outputMixer;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}
}
