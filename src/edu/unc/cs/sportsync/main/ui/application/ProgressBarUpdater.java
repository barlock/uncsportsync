package edu.unc.cs.sportsync.main.ui.application;

/*************************************************************************************
 * 
 * Author(s) - Michael Barlock
 * 		   created: October 10, 2012
 * 	 last modified: October 25, 2012
 * 
 * Function - Updates the cache progress bar.  Hides the bar once it is full, and displays
 * 		it when the cache is filling up.
 * 
 * 
 *************************************************************************************/
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
