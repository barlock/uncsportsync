package edu.unc.cs.sportsync.main.ui.settings;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.Mixer;

import org.eclipse.e4.xwt.DefaultLoadingContext;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.annotation.UI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Spinner;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.sound.AudioControl;
import edu.unc.cs.sportsync.main.ui.error.ErrorUtil;

public class AudioSettingsTab extends Composite {

	private ArrayList<Mixer.Info> inputMixerInfo;
	private ArrayList<Mixer.Info> outputMixerInfo;
	private Settings settings;
	private AudioControl audioControl;

	private final int MAX_DELAY = 120;

	@UI
	Combo inputDeviceCombo;

	@UI
	Combo outputDeviceCombo;

	@UI
	Spinner maxDelaySpinner;

	@UI
	ProgressBar inputLevelBar;

	@UI
	Button testButton;

	@UI
	Button applyButton;

	private final LineListener testAudioListener = new LineListener() {
		@Override
		public void update(LineEvent event) {
			if (event.getType() == LineEvent.Type.STOP) {
				audioControl.testAudioDonePlaying();
				getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						stopTestAudio();
					}
				});
			}
		}
	};

	public AudioSettingsTab(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());

		// load XWT
		String name = AudioSettingsTab.class.getSimpleName() + IConstants.XWT_EXTENSION_SUFFIX;
		try {
			URL url = AudioSettingsTab.class.getResource(name);
			Map<String, Object> options = new HashMap<String, Object>();
			options.put(IXWTLoader.CLASS_PROPERTY, this);
			options.put(IXWTLoader.CONTAINER_PROPERTY, this);
			XWT.setLoadingContext(new DefaultLoadingContext(this.getClass().getClassLoader()));
			XWT.loadWithOptions(url, options);
		} catch (Throwable e) {
			throw new Error("Unable to load " + name, e);
		}

		updateDeviceLevels();
	}

	private String[] getInputDeviceNames() {
		/*
		 * inputMixerInfo = new
		 * ArrayList<Mixer.Info>(AudioControl.getInputDevices());
		 */
		inputMixerInfo = AudioControl.getTargetMixers();

		String[] names = new String[inputMixerInfo.size()];

		for (int i = 0; i < inputMixerInfo.size(); i++) {
			names[i] = inputMixerInfo.get(i).getName();
		}

		return names;
	}

	private String[] getOutputDeviceNames() {
		/*
		 * outputMixerInfo = new
		 * ArrayList<Mixer.Info>(AudioControl.getOutputDevices());
		 */
		outputMixerInfo = AudioControl.getSourceMixers();

		String[] names = new String[outputMixerInfo.size()];

		for (int i = 0; i < outputMixerInfo.size(); i++) {
			names[i] = outputMixerInfo.get(i).getName();
		}

		return names;
	}

	public boolean hasMaxDelaySpinnerChanged() {
		return settings.getMaxDelay() != maxDelaySpinner.getSelection();
	}

	public void setApplyButtonListener(Listener audioApplyButtonListener) {
		applyButton.addListener(SWT.Selection, audioApplyButtonListener);
	}

	public void setAudioControl(AudioControl control) {
		audioControl = control;
		audioControl.setTestAudioListener(testAudioListener);
	}

	public void setSettings(Settings settings) {

		this.settings = settings;
		inputDeviceCombo.setItems(getInputDeviceNames());
		inputDeviceCombo.select(inputMixerInfo.indexOf(settings.getInputMixer()));
		outputDeviceCombo.setItems(getOutputDeviceNames());
		outputDeviceCombo.select(outputMixerInfo.indexOf(settings.getOutputMixer()));

		maxDelaySpinner.setMaximum(MAX_DELAY);
		maxDelaySpinner.setSelection(settings.getMaxDelay());
	}

	public void setTestButtonListener(Listener audioTestButtonListener) {
		testButton.addListener(SWT.Selection, audioTestButtonListener);
	}

	public void updateDeviceLevels() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(60);
					} catch (Throwable th) {
						ErrorUtil.openStackTraceDialog("A Fatal Error has occured and the application will need to shut down", th);
						System.exit(1);
					}
					if (isDisposed()) {
						return;
					}

					getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							if (!isDisposed()) {
								inputLevelBar.setSelection(audioControl.getInputLevel());
							}
						}

					});
				}
			}
		}.start();
	}

	public void updateSettings() {
		settings.setMaxDelay(maxDelaySpinner.getSelection());
		settings.setInputMixer(inputMixerInfo.get(inputDeviceCombo.getSelectionIndex()));
		settings.setOutputMixer(outputMixerInfo.get(outputDeviceCombo.getSelectionIndex()));
	}

	public void onTestButtonSelection(Event event) {
		if (testButton.getSelection()) {
			audioControl.startTestOutput();
			testButton.setText("Stop");
		} else {
			audioControl.stopTestOutput();
			testButton.setText("Test");
		}

	}

	public void stopTestAudio() {
		testButton.setText("Test");
		testButton.setSelection(false);
		audioControl.stopTestOutput();
	}

	public void onDispose(Event event) {
		audioControl.stopTestOutput();
		stopTestAudio();
	}
}
