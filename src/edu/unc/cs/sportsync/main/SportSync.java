package edu.unc.cs.sportsync.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.unc.cs.sportsync.main.ui.application.Application;

public class SportSync {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // ApplicationPresenter app = new ApplicationPresenter();
        // app.open();
        // app.dispose();

        // Uncomment below and comment above to switch from XWT to our previous
        // MVP

        Display display = new Display();
        Shell application = new Shell(display);
        FillLayout layout = new FillLayout();

        application.setLayout(layout);
        application.setSize(450, 280);
        application.setLocation(400, 300);
        application.setText("UNC SportSync");

        Application appComposite = new Application(application, SWT.None);

        application.open();

        while (!application.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        application.dispose();
    }
}
