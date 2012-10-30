package edu.unc.cs.sportsync.main.ui.application;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.xwt.DefaultLoadingContext;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.annotation.UI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.sound.AudioControl;
import edu.unc.cs.sportsync.main.ui.error.ErrorUtil;
import edu.unc.cs.sportsync.main.ui.settings.SettingsDialog;

public class Application extends Composite {
	@UI
	Button startButton;

	@UI
	Button settingsButton;

	@UI
	Button muteButton;

	@UI
	Label delayValueLabel;

	@UI
	Scale volumeScale;

	@UI
	Scale delayScale;

	@UI
	Label maxDelayLabel;

	@UI
	ProgressBar audioBar;

	@UI
	ProgressBar bufferProgressBar;

	private final Settings settings;
	private final AudioControl audioControl;

	private SettingsDialog settingsComposite;
	private Shell settingsDialog;

	private final Image muteOnImg;
	private final Image muteOffImg;

	private final Listener saveButtonListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			updateUI();
			settingsDialog.close();
		}
	};

	private final Listener audioApplyButtonListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			updateUI();
			settingsComposite.getAudioPage().toggleTestButtonText();
		}
	};

	public Application(Composite parent, int style) {
		super(parent, style);
		audioControl = new AudioControl();
		audioControl.prepareMixerList();
		settings = new Settings();
		setLayout(new FillLayout());

		audioControl.setSettings(settings);

		// load XWT
		String name = Application.class.getSimpleName() + IConstants.XWT_EXTENSION_SUFFIX;
		try {
			URL url = Application.class.getResource(name);
			Map<String, Object> options = new HashMap<String, Object>();
			options.put(IXWTLoader.CLASS_PROPERTY, this);
			options.put(IXWTLoader.CONTAINER_PROPERTY, this);
			XWT.setLoadingContext(new DefaultLoadingContext(this.getClass().getClassLoader()));
			XWT.loadWithOptions(url, options);
		} catch (Throwable e) {
			throw new Error("Unable to load " + name, e);
		}

		muteOnImg = new Image(getDisplay(), new ImageData(Application.class.getResourceAsStream("volume.png")));
		muteOffImg = new Image(getDisplay(), new ImageData(Application.class.getResourceAsStream("mute.png")));
		muteButton.setImage(muteOnImg);

		bufferProgressBarRun();

		updateDelayTime();

		audioControl.start();
		audioControl.setVolume(settings.getVolume());
		audioControl.setDelayAmount(delayScale.getSelection());
		volumeScale.setSelection(settings.getVolume());
	}

	private void bufferProgressBarRun() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (Throwable th) {
						ErrorUtil.openStackTraceDialog("A Fatal Error has occured and the application will need to shut down", th);
						System.exit(1);
					}
					if (isDisposed()) {
						return;
					}

					int percent = audioControl.getBufferPercentage();

					getDisplay().asyncExec(new ProgressBarUpdater(bufferProgressBar, percent));
				}
			}
		}.start();
	}

	public void onDelayScaleDragDetect(Event event) {
		setDelayAmountText(delayScale.getSelection() / 10.0);
		audioControl.setDelayAmount(delayScale.getSelection());
	}

	public void onDispose(Event event) {
		settings.save();
		audioControl.closeLines();
	}

	public void onMuteButtonSelection(Event event) {
		audioControl.toggleMute();
		updateMuteButton();
	}

	public void onSettingsButtonSelection(Event event) {
		if (settingsDialog == null || settingsDialog.isDisposed()) {
			settingsDialog = new Shell(getShell(), SWT.DIALOG_TRIM);
			FillLayout layout = new FillLayout();
			settingsDialog.setText("Settings");
			settingsDialog.setLayout(layout);
			settingsDialog.setSize(450, 300);
			settingsComposite = new SettingsDialog(settingsDialog, SWT.NONE, settings, audioApplyButtonListener, saveButtonListener, audioControl);

			settingsDialog.open();
		}
	}

	public void onVolumeScaleDragDetect(Event event) {
		int volume = volumeScale.getSelection();
		audioControl.setVolume(volume);
		settings.setVolume(volume);
	}

	public void setDelayAmountText(double amount) {
		delayValueLabel.setText("Delay: " + amount + " seconds");
		delayValueLabel.pack();
	}

	private void updateDelayTime() {
		maxDelayLabel.setText(settings.getMaxDelay() + " sec");
		delayScale.setMaximum(settings.getMaxDelay() * 10);
		maxDelayLabel.pack();

	}

	private void updateMuteButton() {
		boolean isPressed = muteButton.getSelection();
		if (isPressed) {
			muteButton.setImage(muteOffImg);
		} else {
			muteButton.setImage(muteOnImg);
		}
	}

	private void updateUI() {
		audioControl.stopTestOutput();

		if (settingsComposite.hasMaxDelayChanged()) {
			delayScale.setSelection(0);
			updateDelayTime();
			audioControl.resetBuffer();
		}

		settingsComposite.updateSettings();
		updateDelayTime();
		setDelayAmountText(delayScale.getSelection() / 10.0);
		settings.save();
		audioControl.updateLines();
	}
}
