package edu.unc.cs.sportsync.main.sound;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import edu.unc.cs.sportsync.main.ui.settings.AudioSettingsTab;

public class TestLine {
	private final Mixer outputMixer;
	private final Mixer.Info outputMixerInfo;
	private final AudioControl audioControl;

	public TestLine(Mixer.Info mixerInfo, AudioControl audioControl) {
		this.outputMixerInfo = mixerInfo;
		this.audioControl = audioControl;
		outputMixer = AudioSystem.getMixer(outputMixerInfo);
	}

	private SourceDataLine openOutputLine() throws LineUnavailableException {
		SourceDataLine outputLine = null;

		outputMixer.open();

		Line.Info[] sourcelineinfos = outputMixer.getSourceLineInfo();
		for (int i = 0; i < sourcelineinfos.length; i++) {
			if (sourcelineinfos[i].getLineClass() == SourceDataLine.class) {
				System.out.println(sourcelineinfos[i]);
				outputLine = (SourceDataLine) AudioSystem.getLine(sourcelineinfos[i]);
				break;
			}
		}

		outputMixer.close();

		if (outputLine == null) {
			throw new LineUnavailableException();
		}

		outputLine.open(audioControl.getAudioFormat(), AudioControl.BUFFER_SIZE);

		outputLine.start();

		return outputLine;
	}

	public void outputTestWav() {
		new Thread() {
			@Override
			public void run() {
				// This cant be entirely inside the asyncExec...
				AudioInputStream audioInputStream = null;
				SourceDataLine outputLine = null;
				try {
					try {
						audioInputStream = AudioSystem.getAudioInputStream(AudioSettingsTab.class.getResourceAsStream("test.wav"));

						outputLine = openOutputLine();
					} catch (LineUnavailableException e1) {
						e1.printStackTrace();
					} catch (UnsupportedAudioFileException e) {
						e.printStackTrace();
					}

					int nBytesRead = 0;
					byte[] abData = new byte[AudioControl.BUFFER_SIZE];

					while (nBytesRead != -1) {
						nBytesRead = audioInputStream.read(abData, 0, abData.length);
						if (nBytesRead >= 0) {
							outputLine.write(abData, 0, nBytesRead);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}

				outputLine.drain();
				outputLine.close();
			}
		}.start();
	}
}