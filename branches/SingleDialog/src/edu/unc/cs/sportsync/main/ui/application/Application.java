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
import org.eclipse.swt.widgets.ToolItem;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.sound.AudioControl;
import edu.unc.cs.sportsync.main.ui.settings.SettingsDialog;

public class Application extends Composite {
	@UI
	ToolItem settingsButton;

	@UI
	ToolItem muteButton;

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

	public final Settings settings;
	public final AudioControl audioControl;

	private SettingsDialog settingsComposite;
	private Shell settingsDialog;

	private Image muteOnImg;
	private Image muteOffImg;

	public final Listener applyButtonListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			settingsComposite.updateSettings();
			updateDelayTime();
			setDelayAmountText(delayScale.getSelection() / 10.0);
			settings.save();
			settingsDialog.close();
		}
	};

	public Application(Composite parent, int style) {
		super(parent, style);
		settings = new Settings();
		audioControl = new AudioControl();
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
		volumeScale.setSelection(settings.getVolume());
		delayScale.setFocus();
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
	
	public void updateUI() {
		setDelayAmountText(delayScale.getSelection() / 10.0);
		updateDelayTime();
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
	}

	private void updateMuteButton() {
		boolean isPressed = muteButton.getSelection();
		if (isPressed) {
			muteButton.setImage(muteOffImg);
		} else {
			muteButton.setImage(muteOnImg);
		}
	}

	public void setSettingsButtonListener(Listener settingsButtonlistener) {
		settingsButton.addListener(SWT.Selection, settingsButtonlistener);
	}
	
	public void startAudio() {
		if (!audioControl.isRecording()) {
			audioControl.start();
			audioControl.setVolume(settings.getVolume());
			audioControl.setDelayAmount(delayScale.getSelection());
			if (audioControl.isMuted()) {
				audioControl.toggleMute();
			}
		} else {
			audioControl.stopRecording();
		}
	}
	
	private static ImageData rotate(ImageData srcData, int direction) {
		int bytesPerPixel = srcData.bytesPerLine / srcData.width;
		int destBytesPerLine = (direction == SWT.DOWN)? srcData.width * bytesPerPixel : srcData.height * bytesPerPixel;
		byte[] newData = new byte[(direction == SWT.DOWN)? srcData.height * destBytesPerLine : srcData.width * destBytesPerLine];
		int width = 0, height = 0;
		for (int srcY = 0; srcY < srcData.height; srcY++) {
			for (int srcX = 0; srcX < srcData.width; srcX++) {
				int destX = 0, destY = 0, destIndex = 0, srcIndex = 0;
				switch (direction){
					case SWT.LEFT: // left 90 degrees
						destX = srcY;
						destY = srcData.width - srcX - 1;
						width = srcData.height;
						height = srcData.width; 
						break;
					case SWT.RIGHT: // right 90 degrees
						destX = srcData.height - srcY - 1;
						destY = srcX;
						width = srcData.height;
						height = srcData.width; 
						break;
					case SWT.DOWN: // 180 degrees
						destX = srcData.width - srcX - 1;
						destY = srcData.height - srcY - 1;
						width = srcData.width;
						height = srcData.height; 
						break;
				}
				destIndex = (destY * destBytesPerLine) + (destX * bytesPerPixel);
				srcIndex = (srcY * srcData.bytesPerLine) + (srcX * bytesPerPixel);
				System.arraycopy(srcData.data, srcIndex, newData, destIndex, bytesPerPixel);
			}
		}
		// destBytesPerLine is used as scanlinePad to ensure that no padding is required
		return new ImageData(width, height, srcData.depth, srcData.palette, srcData.scanlinePad, newData);
	}
}
