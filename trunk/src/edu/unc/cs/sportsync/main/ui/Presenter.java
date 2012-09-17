package edu.unc.cs.sportsync.main.ui;

public abstract class Presenter {

    public Presenter() {
	init();
    }

    protected abstract void initListners();

    protected abstract void addListeners();

    protected abstract void init();

}
