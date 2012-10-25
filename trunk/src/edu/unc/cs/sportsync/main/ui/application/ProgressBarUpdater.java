package edu.unc.cs.sportsync.main.ui.application;

import org.eclipse.swt.widgets.ProgressBar;

public class ProgressBarUpdater implements Runnable {

	private final ProgressBar bufferProgressBar;
	private final int percent;

	public ProgressBarUpdater(ProgressBar bar, int p) {
		bufferProgressBar = bar;
		percent = p;
	}

	@Override
	public void run() {
		if (percent == 100) {
			bufferProgressBar.setVisible(false);
		} else {
			bufferProgressBar.setVisible(true);
			bufferProgressBar.setSelection(percent);
		}
	}

}
