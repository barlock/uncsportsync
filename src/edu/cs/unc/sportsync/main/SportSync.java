package edu.cs.unc.sportsync.main;

import org.eclipse.swt.widgets.Display;

import edu.cs.unc.sportsync.main.ui.Application;

public class SportSync {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		new Application(display);
		display.dispose();
	}

}
