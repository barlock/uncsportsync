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

    private boolean isRecording = false;
    private boolean isMuted = false;

    private final Settings settings;
    private final AudioControl audioControl;

    private SettingsDialog settingsComposite;
    private Shell settingsDialog;

    private Image muteOnImg;
    private Image muteOffImg;

    private final Listener applyButtonListener = new Listener() {
        @Override
        public void handleEvent(Event event) {
            settingsComposite.updateSettings();
            updateDelayTime();
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
            muteOnImg = new Image(getDisplay(), Application.class.getResourceAsStream("mute3.gif"));
            muteOffImg = new Image(getDisplay(), Application.class.getResourceAsStream("mute4.gif"));
            muteButton.setImage(muteOnImg);
        } catch (Exception e) {
            muteButton.setText("Mute");
        }

        updateDelayTime();
        volumeScale.setSelection(settings.getVolume());
    }

    public void onDelayScaleDragDetect(Event event) {
        setDelayAmountText(delayScale.getSelection() / 10.0);
        if (isRecording) {
            audioControl.setDelayAmount(delayScale.getSelection());
        }
    }

    public void onDispose(Event event) {
        settings.save();

        if (isRecording) {
            audioControl.stopRecording();
        }
    }

    public void onMuteButtonSelection(Event event) {
        isMuted = !isMuted;
        if (isRecording) {
            audioControl.toggleMute();
        }
        updateMuteButton();
    }

    public void onSettingsButtonSelection(Event event) {
        if (settingsDialog == null || settingsDialog.isDisposed()) {
            settingsDialog = new Shell(getShell());
            FillLayout layout = new FillLayout();
            settingsDialog.setText("Settings");
            settingsDialog.setLayout(layout);
            settingsDialog.setSize(500, 500);

            settingsComposite = new SettingsDialog(settingsDialog, SWT.DIALOG_TRIM, settings, applyButtonListener);

            if (isRecording) {
                audioControl.stopRecording();
                isRecording = false;
                startButton.setText("Start");
            }

            settingsDialog.open();
        }
    }

    public void onStartButtonSelection(Event event) {
        if (!isRecording) {
            audioControl.start();
            audioControl.setVolume(settings.getVolume());
            audioControl.setDelayAmount(delayScale.getSelection());
            if (isMuted) {
                audioControl.toggleMute();
            }
            isRecording = true;

            startButton.setText("Stop");
        } else {
            audioControl.stopRecording();
            startButton.setText("Start");
        }
    }

    public void onVolumeScaleDragDetect(Event event) {
        int volume = volumeScale.getSelection();
        if (isRecording) {
            audioControl.setVolume(volume);
        }
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
}
