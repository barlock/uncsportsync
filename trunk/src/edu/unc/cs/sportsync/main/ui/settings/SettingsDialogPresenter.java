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

    private final Listener delayTimeChangeListener;
    private Listener okButtonListener;
    private Listener outputSelectionListener;
    private Listener inputSelectionListener;

    public SettingsDialogPresenter(Shell app, Settings settings, Listener delayTimeChangeListener) {
        application = app;
        this.settings = settings;
        this.delayTimeChangeListener = delayTimeChangeListener;

        view = new SettingsDialogView(application, settings);

        view.setInputDeviceNames(getInputDeviceNames());
        view.setOutputDeviceNames(getOutputDeviceNames());
        view.setDelayListTimes(getDelayListTimes());

        initListners();
    }

    @Override
    public void addListeners() {
        view.addDelayTimeChangeListener(delayTimeChangeListener);
        view.addOutputSelectionListener(outputSelectionListener);
        view.addInputSelectionListener(inputSelectionListener);
        view.addOkButtonListener(okButtonListener);
    }

    private String[] getDelayListTimes() {
        // Optional Delay times
        String[] listOfDelayTimes = new String[40];
        String time;
        for (int i = 0; i < 40; i++) {
            time = (i + 1) / 4 == 0 ? String.format("%d sec", 15 * ((i + 1) % 4)) : (15 * ((i + 1) % 4) == 0 ? String.format("%d min", (i + 1) / 4) : String.format("%d min & %d sec", (i + 1) / 4,
                    15 * ((i + 1) % 4)));
            listOfDelayTimes[i] = time;
        }

        return listOfDelayTimes;
    }

    private String[] getInputDeviceNames() {
        ArrayList<Info> inputDevices = new ArrayList<Info>(AudioControl.getInputDevices());
        String[] names = new String[inputDevices.size()];

        for (int i = 0; i < inputDevices.size(); i++) {
            names[i] = inputDevices.get(i).getName();
        }

        return names;
    }

    private String[] getOutputDeviceNames() {
        ArrayList<Info> outputDevices = new ArrayList<Info>(AudioControl.getOutputDevices());
        String[] names = new String[outputDevices.size()];

        for (int i = 0; i < outputDevices.size(); i++) {
            names[i] = outputDevices.get(i).getName();
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

        okButtonListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                view.close();
            }
        };

        addListeners();
    }

    public void open() {
        view.open();
    }
}
