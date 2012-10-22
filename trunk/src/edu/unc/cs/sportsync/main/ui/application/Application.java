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

	private Image muteOnImg;
	private Image muteOffImg;

	private final Listener applyButtonListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			if (settingsComposite.hasMaxDelayChanged()) {
				delayScale.setSelection(0);
			}

			settingsComposite.updateSettings();
			updateDelayTime();
			setDelayAmountText(delayScale.getSelection() / 10.0);
			settings.save();
			settingsDialog.close();
		}
	};

	public Application(Composite parent, int style) {
		super(parent, style);
		audioControl = new AudioControl();
		audioControl.prepareMixerList();
		settings = new Settings();
		setLayout(new FillLayout());

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

		try {
			muteOnImg = new Image(getDisplay(), Application.class.getResourceAsStream("volume.png"));
			muteOffImg = new Image(getDisplay(), Application.class.getResourceAsStream("mute.png"));
			muteButton.setImage(muteOnImg);
		} catch (Exception e) {
			muteButton.setText("Mute");
		}

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
					}
					if (isDisposed()) {
						return;
					}

					int percent = audioControl.getBufferPercentage();

					getDisplay().asyncExec(new ProgressBarUpdater(bufferProgressBar, percent, audioControl.isRecording()));
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

		audioControl.stopRecording();
	}

	public void onMuteButtonSelection(Event event) {
		audioControl.toggleMute();
		updateMuteButton();
	}

	public void onSettingsButtonSelection(Event event) {
		if (settingsDialog == null || settingsDialog.isDisposed()) {
			settingsDialog = new Shell(getShell());
			FillLayout layout = new FillLayout();
			settingsDialog.setText("Settings");
			settingsDialog.setLayout(layout);
			settingsDialog.setSize(500, 500);
			settingsComposite = new SettingsDialog(settingsDialog, SWT.DIALOG_TRIM, settings, applyButtonListener, audioControl);

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
		maxDelayLabel.setText(settings.getDelayTime() + " sec");
		delayScale.setMaximum(settings.getDelayTime() * 10);

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
}
