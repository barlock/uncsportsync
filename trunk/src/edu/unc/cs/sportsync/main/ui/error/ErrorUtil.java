package edu.unc.cs.sportsync.main.ui.error;

/*************************************************************************************
 * 
 * Author(s) - Michael Barlock
 * 		   created: October 29, 2012
 * 	 last modified: October 29, 2012
 * 
 * Function - Creates the error dialog box.
 * 
 * 
 *************************************************************************************/
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;

public class ErrorUtil {

	/**
	 * Shows JFace ErrorDialog but improved by constructing full stack trace in
	 * detail area.
	 */
	public static void openStackTraceDialog(String msg, Throwable t) {

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);

		final String trace = sw.toString(); // stack trace as a string

		// Temp holder of child statuses
		List<Status> childStatuses = new ArrayList<Status>();

		// Split output by OS-independend new-line
		for (String line : trace.split(System.getProperty("line.separator"))) {
			// build & add status
			childStatuses.add(new Status(IStatus.ERROR, "SportSync", line));
		}

		// convert to array of statuses
		MultiStatus ms = new MultiStatus("SportSync", IStatus.ERROR, childStatuses.toArray(new Status[] {}), t.getLocalizedMessage(), t);

		ErrorDialog.openError(null, "Fatal Error", msg, ms);
	}

}
