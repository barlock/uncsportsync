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

    private Listener delayTimeChangeListener;
    private Listener applyButtonListener;
    private Listener cancelButtonListener;
    private Listener outputSelectionListener;
    private Listener inputSelectionListener;
    
    private final int MAX_DELAY = 120;

    public SettingsDialogPresenter(Shell app, Settings settings, Listener delayTimeChangeListener) {
        application = app;
        this.settings = settings;
        this.delayTimeChangeListener = delayTimeChangeListener;

        view = new SettingsDialogView(application, settings);

        view.setInputDeviceNames(getInputDeviceNames());
        view.setOutputDeviceNames(getOutputDeviceNames());
        view.setDelayListTimes(MAX_DELAY);

        initListners();
    }

    @Override
    public void addListeners() {
        view.addDelayTimeChangeListener(delayTimeChangeListener);
        view.addOutputSelectionListener(outputSelectionListener);
        view.addInputSelectionListener(inputSelectionListener);
        view.addApplyButtonListener(applyButtonListener);
        view.addCancelButtonListener(cancelButtonListener);
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
    
    public void applyChanges(){
    	settings.delayTime = view.getDelayList().getSelection();
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

        applyButtonListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
            	applyChanges();
                view.close();
            }
        };
        
        cancelButtonListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                view.close();
            }
        };
        
        delayTimeChangeListener = new Listener() {
        	@Override
        	public void handleEvent (Event event) {
        		System.out.println("Delay Value -> " + view.getDelayList().getSelection());
        	}
        };

        addListeners();
    }

    public void open() {
        view.open();
    }
}
