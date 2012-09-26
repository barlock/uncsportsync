package edu.unc.cs.sportsync.main.ui.application;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.sound.AudioControl;
import edu.unc.cs.sportsync.main.ui.IPresenter;
import edu.unc.cs.sportsync.main.ui.settings.SettingsDialogPresenter;

public class ApplicationPresenter implements IPresenter {
    private final ApplicationView view;

    private Listener settingsButtonListener;
    private Listener muteButtonListener;
    private Listener sliderListener;
    private Listener volumeSliderListener;
    private Listener applyButtonListeners;
    private SelectionAdapter exitAdapter;

    private final Settings settings;

    private SettingsDialogPresenter settingsPresenter;

    public ApplicationPresenter() {
        settings = new Settings();
        view = new ApplicationView(settings);

        initListners();
        AudioControl.start();
    }

    @Override
    public void addListeners() {
        view.addSettingsButtonListener(settingsButtonListener);
        view.addMuteButtonListener(muteButtonListener);
        view.addExitListener(exitAdapter);
        view.addSliderListener(sliderListener);
        view.addVolumeSliderListener(volumeSliderListener);
    }

    public void dispose() {
        settings.save();
        AudioControl.stopRecording();
        view.dispose();
    }

    @Override
    public void initListners() {
        applyButtonListeners = new Listener() {

            @Override
            public void handleEvent(Event event) {
                settingsPresenter.updateSettings();
                view.updateDelayTime();
                settingsPresenter.close();
            }
        };

        settingsButtonListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (settingsPresenter == null || settingsPresenter.isDisposed()) {
                    settingsPresenter = new SettingsDialogPresenter(view.getApplication(), view.getSettings(), applyButtonListeners);
                    settingsPresenter.open();
                }
            }
        };
        muteButtonListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                AudioControl.toggleMute();
            }
        };
        sliderListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                view.setDelayAmountText(view.getScale().getSelection() / 10.0);
            }
        };
        volumeSliderListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                AudioControl.setVolume(100 - view.getVolumeScale().getSelection());
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
