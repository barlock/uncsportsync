package edu.unc.cs.sportsync.main.ui.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.sound.AudioControl;
import edu.unc.cs.sportsync.main.ui.IPresenter;
import edu.unc.cs.sportsync.main.ui.settings.SettingsDialog;

public class ApplicationPresenter implements IPresenter {
    private final ApplicationView view;

    private Listener settingsButtonListener;
    private Listener onoffButtonListener;
    private Listener muteButtonListener;
    private Listener sliderListener;
    private Listener volumeSliderListener;
    private Listener applyButtonListener;
    private SelectionAdapter exitAdapter;

    private boolean isRecording = false;
    private boolean isMuted = false;

    private final Settings settings;
    private final AudioControl audioControl;

    private SettingsDialog settingsComposite;
    private Shell settingsDialog;

    public ApplicationPresenter() {
        settings = new Settings();
        audioControl = new AudioControl();
        view = new ApplicationView(settings);
        initListners();
    }

    @Override
    public void addListeners() {
        view.addSettingsButtonListener(settingsButtonListener);
        view.addOnOffButtonListener(onoffButtonListener);
        view.addMuteButtonListener(muteButtonListener);
        view.addExitListener(exitAdapter);
        view.addSliderListener(sliderListener);
        view.addVolumeSliderListener(volumeSliderListener);
    }

    public void dispose() {
        settings.save();
        view.dispose();
        if (isRecording) {
            audioControl.stopRecording();
        }
    }

    @Override
    public void initListners() {
        applyButtonListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                settingsComposite.updateSettings();
                view.updateDelayTime();
                settings.save();
                settingsDialog.close();
            }
        };

        // settingsButtonListener = new Listener() {
        // @Override
        // public void handleEvent(Event event) {
        // if (settingsPresenter == null || settingsPresenter.isDisposed()) {
        // settingsPresenter = new
        // SettingsDialogPresenter(view.getApplication(), view.getSettings(),
        // applyButtonListeners);
        // if (isRecording) {
        // AudioControl.stopRecording();
        // isRecording = false;
        // }
        // settingsPresenter.open();
        // }
        // }
        // };

        settingsButtonListener = new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (settingsDialog == null || settingsDialog.isDisposed()) {
                    settingsDialog = new Shell(view.getApplication());
                    FillLayout layout = new FillLayout();
                    settingsDialog.setText("Settings");
                    settingsDialog.setLayout(layout);
                    settingsDialog.setSize(500, 500);

                    settingsComposite = new SettingsDialog(settingsDialog, SWT.DIALOG_TRIM, settings, applyButtonListener, audioControl);

                    if (isRecording) {
                        audioControl.stopRecording();
                        isRecording = false;
                    }
                    settingsDialog.open();

                }
            }

        };

        onoffButtonListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (!isRecording) {
                    audioControl.start();
                    audioControl.setVolume(settings.getVolume());
                    audioControl.setDelayAmount(view.getScale().getSelection());
                    if (isMuted) {
                        audioControl.toggleMute();
                    }
                    isRecording = true;
                }

            }
        };
        muteButtonListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                isMuted = !isMuted;
                if (isRecording) {
                    audioControl.toggleMute();
                }
            }
        };
        sliderListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                view.setDelayAmountText(view.getScale().getSelection() / 10.0);
                if (isRecording) {
                    audioControl.setDelayAmount(view.getScale().getSelection());
                }
            }
        };
        volumeSliderListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                int volume = view.getVolumeScale().getSelection();
                if (isRecording) {
                    audioControl.setVolume(volume);
                    settings.setVolume(volume);
                }
            }
        };
        exitAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                view.exit();
            }
        };

        addListeners();
    }

    @Override
    public void open() {
        view.open();
    }
}
