package edu.unc.cs.sportsync.main.ui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ApplicationPresenter extends Presenter {
    private ApplicationView view;

    private Listener settingsButtonListener;
    private Listener sliderListener;
    private SelectionAdapter exitAdapter;

    @Override
    protected void init() {
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
	settingsButtonListener = new Listener() {
	    public void handleEvent(Event event) {
		view.openSettingsDialog();
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
