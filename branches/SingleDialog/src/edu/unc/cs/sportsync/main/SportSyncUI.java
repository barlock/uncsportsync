package edu.unc.cs.sportsync.main;

import java.net.URL;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.annotation.UI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import edu.unc.cs.sportsync.main.ui.application.Application;
import edu.unc.cs.sportsync.main.ui.settings.SettingsDialog;
import org.eclipse.swt.widgets.Event;

public class SportSyncUI {
	private static Application main;
	private static SettingsDialog settings;
	private static StackLayout layout;
	private static Shell shell;

	public static void main(String args[]) throws Exception {
		URL url = SportSyncUI.class.getResource(SportSyncUI.class.getSimpleName() + IConstants.XWT_EXTENSION_SUFFIX);
		Control control = XWT.load(url);
		
		shell = control.getShell();
		shell.layout();
		centerInDisplay(shell);
		layout = (StackLayout) shell.getLayout();
		
		main = new Application(shell, SWT.None);
		main.setSettingsButtonListener(settingsButtonListner);
		
		settings = new SettingsDialog(shell, SWT.NONE, main.settings, backButtonListener, main.audioControl);
		
		layout.topControl = main;
		shell.pack();
		
		// run events loop
		shell.open();
		main.startAudio();
		
		
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch())
				shell.getDisplay().sleep();
		}
	}
	
	private final static Listener settingsButtonListner = new Listener() {

		@Override
		public void handleEvent(Event event) {
			layout.topControl = settings;
			shell.layout();
		}
		
	};
	
	private final static Listener backButtonListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			settings.updateSettings();
			main.updateUI();
			main.settings.save();
			layout.topControl = main;
			shell.layout();
		}
		
	};

	private static void centerInDisplay(Shell shell) {
		Rectangle displayArea = shell.getDisplay().getClientArea();
		shell.setBounds(displayArea.width / 4, displayArea.height / 4,
				displayArea.width / 2, displayArea.height / 2);
	}
}
