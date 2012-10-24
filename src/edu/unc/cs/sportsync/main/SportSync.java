package edu.unc.cs.sportsync.main;

import java.net.URL;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import edu.unc.cs.sportsync.main.ui.application.Application;

public class SportSync {
	public static void main(String args[]) throws Exception {
		URL url = SportSync.class.getResource(SportSync.class.getSimpleName()
				+ IConstants.XWT_EXTENSION_SUFFIX);
		Control control = XWT.load(url);
		Shell shell = control.getShell();
		shell.layout();
		centerInDisplay(shell);
		// run events loop

		shell.setSize(400, 250);
		shell.setMinimumSize(400, 250);
		Application appComposite = new Application(shell, SWT.None);

		shell.open();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}

		appComposite.dispose();
	}

	private static void centerInDisplay(Shell shell) {
		Rectangle displayArea = shell.getDisplay().getClientArea();
		shell.setBounds(displayArea.width / 4, displayArea.height / 4,
				displayArea.width / 2, displayArea.height / 2);
	}
}
