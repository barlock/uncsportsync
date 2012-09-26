package edu.unc.cs.sportsync.main.ui.settings;

import java.util.ArrayList;

import javax.sound.sampled.Mixer.Info;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.sound.AudioControl;
import edu.unc.cs.sportsync.main.ui.IPresenter;

public class SettingsDialogPresenter implements IPresenter {

    private final SettingsDialogView view;
    private final Settings settings;
    private final Shell application;

    private Listener applyButtonListener;
    private Listener cancelButtonListener;
    private Listener outputSelectionListener;
    private Listener inputSelectionListener;

    private ArrayList<Info> inputMixerInfo;
    private ArrayList<Info> outputMixerInfo;

    private final int MAX_DELAY = 120;

    public SettingsDialogPresenter(Shell app, Settings settings, Listener applyButtonListner) {
        application = app;
        this.settings = settings;
        this.applyButtonListener = applyButtonListner;

        view = new SettingsDialogView(application, settings);

        view.setInputDeviceNames(getInputDeviceNames());
        view.setOutputDeviceNames(getOutputDeviceNames());
        view.setDelayListTimes(MAX_DELAY);

        initListners();
    }

    @Override
    public void addListeners() {
        view.addOutputSelectionListener(outputSelectionListener);
        view.addInputSelectionListener(inputSelectionListener);
        view.addApplyButtonListener(applyButtonListener);
        view.addCancelButtonListener(cancelButtonListener);
    }

    public void updateSettings() {
        settings.setDelayTime(view.getDelayList().getSelection());
        settings.setInputMixer(inputMixerInfo.get(view.getInputList().getSelectionIndex()));
        settings.setOutputMixer(outputMixerInfo.get(view.getOutputList().getSelectionIndex()));
    }

    private String[] getInputDeviceNames() {
        inputMixerInfo = new ArrayList<Info>(AudioControl.getInputDevices());
        String[] names = new String[inputMixerInfo.size()];

        for (int i = 0; i < inputMixerInfo.size(); i++) {
            names[i] = inputMixerInfo.get(i).getName();
        }

        return names;
    }

    private String[] getOutputDeviceNames() {
        outputMixerInfo = new ArrayList<Info>(AudioControl.getOutputDevices());
        String[] names = new String[outputMixerInfo.size()];

        for (int i = 0; i < outputMixerInfo.size(); i++) {
            names[i] = outputMixerInfo.get(i).getName();
        }

        return names;
    }

    @Override
    public void initListners() {
        outputSelectionListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                System.out.println("Output Selection detail -> " + view.getOutputList().getText());
            }

        };

        inputSelectionListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                System.out.println("Input Selection detail -> " + view.getInputList().getText());
            }
        };

        cancelButtonListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                view.close();
            }
        };

        addListeners();
    }

    @Override
    public void open() {
        view.open();
    }

    public void close() {
        view.close();
    }

    public boolean isDisposed() {
        return view.isDisposed();
    }
}
