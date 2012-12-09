package edu.unc.cs.sportsync.main;

/*************************************************************************************
 * 
 * Author(s) - Michael Barlock, Kartik Sethuraman, Patrick Waivers
 * 		   created: October 2, 2012
 * 	 last modified: October 26, 2012
 * 
 * Function - Contains the main function for the application.  Launches a shell with the UI
 *     set up by an Application object.
 * 
 * 
 *************************************************************************************/
import java.net.URL;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import edu.unc.cs.sportsync.main.ui.application.Application;
import edu.unc.cs.sportsync.main.ui.error.ErrorUtil;

public class SportSync {
	private static void centerInDisplay(Shell shell) {
		Rectangle displayArea = shell.getDisplay().getClientArea();
		shell.setBounds(displayArea.width / 4, displayArea.height / 4, displayArea.width / 2, displayArea.height / 2);
	}

	public static void main(String args[]) {
		Shell shell = new Shell();
		try {
			URL url = SportSync.class.getResource(SportSync.class.getSimpleName() + IConstants.XWT_EXTENSION_SUFFIX);
			Control control = XWT.load(url);
			shell = control.getShell();
			shell.layout();
			centerInDisplay(shell);
			// run events loop
			Image logo512 = new Image(shell.getDisplay(), SportSync.class.getResourceAsStream("sportsync512.png"));
			Image logo256 = new Image(shell.getDisplay(), SportSync.class.getResourceAsStream("sportsync256.png"));
			Image logo48 = new Image(shell.getDisplay(), SportSync.class.getResourceAsStream("sportsync48.png"));
			Image logo32 = new Image(shell.getDisplay(), SportSync.class.getResourceAsStream("sportsync32.png"));
			Image logo16 = new Image(shell.getDisplay(), SportSync.class.getResourceAsStream("sportsync16.ico"));
			Image[] images = { logo512, logo256, logo48, logo32, logo16 };

			shell.setSize(400, 250);
			shell.setMinimumSize(400, 250);
			shell.setImages(images);
			Application appComposite = new Application(shell, SWT.None);

			shell.open();
			while (!shell.isDisposed()) {
				if (!shell.getDisplay().readAndDispatch()) {
					shell.getDisplay().sleep();
				}
			}
			appComposite.dispose();
			shell.dispose();
		} catch (Exception e) {
			e.printStackTrace();
			ErrorUtil.openStackTraceDialog("A Fatal Error has occured and the application will need to shut down", e);
			System.exit(1);
		}
	}
}
