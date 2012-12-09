package edu.unc.cs.sportsync.main.settings;

/*************************************************************************************
 * 
 * Author(s) - Michael Barlock, Kartik Sethuraman
 * 		   created: September 17, 2012
 * 	 last modified: November 13, 2012
 * 
 * Function - The settings file is the model for reading/creating/updating the settings.xml file.
 * 		   It owns SettingsXMLBuilder and SettingsXMLParser objects which are responsible for 
 *         the actual creation and parsing of the settings.xml file.  Other classes own a Settings
 *         object which provides them with the saved settings (max delay, volume, input mixer,
 *         output mixer.
 * 
 * 
 *************************************************************************************/
import java.io.File;

import javax.sound.sampled.Mixer;

import edu.unc.cs.sportsync.main.sound.AudioControl;

public class Settings {
	private final String PATH;
	private final String APP_DIR = "/.uncsportsync/";
	private final String SETTINGS_NAME = "settings.xml";
	private int maxDelay;
	private int volume;

	private Mixer.Info inputMixer;
	private Mixer.Info outputMixer;

	public Settings() {
		String userHome = System.getProperty("user.home");
		PATH = userHome + APP_DIR + SETTINGS_NAME;

		// Create app_dir if it doesn't exist
		File appDir = new File(userHome + APP_DIR);
		appDir.mkdir();

		File file = new File(PATH);

		boolean failed = false;

		// Init settings to default
		setDefaultSettings();

		if (file.exists()) {
			failed = !parseSettingsFile(file);
		}

		if (failed) {
			System.out.println("failed to parse settings file");
			// do nothing, this is ok
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
			e.printStackTrace();
			// ErrorUtil.openStackTraceDialog("An I/O error occured, the application will shut down",
			// e);
			// System.exit(1);
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
