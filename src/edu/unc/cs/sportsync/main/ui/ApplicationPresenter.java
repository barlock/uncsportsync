package edu.unc.cs.sportsync.main.ui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ApplicationPresenter extends Presenter {
    private ApplicationView view;

    private Listener settingsButtonListener;
    private Listener sliderListener;
    private Listener delayTimeChangeListener;
    private SelectionAdapter exitAdapter;

    private SettingsDialogPresenter settingsPresenter;

    public ApplicationPresenter() {
	view = new ApplicationView();

	initListners();

	view.open();
    }

    @Override
    protected void addListeners() {
	view.addSettingsButtonListener(settingsButtonListener);
	view.addExitListener(exitAdapter);
	view.addSliderListner(sliderListener);
    }

    public void dispose() {
	view.dispose();
    }

    @Override
    protected void initListners() {
	delayTimeChangeListener = new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		view.updateDelayTime();
	    }
	};

	settingsButtonListener = new Listener() {
	    public void handleEvent(Event event) {
		settingsPresenter = new SettingsDialogPresenter(view.getApplication(), view.getSettings(), delayTimeChangeListener);

		settingsPresenter.open();
	    }
	};

	sliderListener = new Listener() {
	    public void handleEvent(Event event) {
		view.setDelayAmountText(view.getScale().getSelection() / 10.0);
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
}
