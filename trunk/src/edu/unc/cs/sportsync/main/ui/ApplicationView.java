package edu.unc.cs.sportsync.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import edu.unc.cs.sportsync.main.settings.Settings;

public class ApplicationView extends View {

    private Shell application;
    private Settings settings;
    private Display display;
    private Scale scale;
    private Combo DelayList;

    private Button settingsButton;
    private MenuItem exitItem;
    private MenuItem cascadeFileMenu;
    private Menu menuBar;
    private Menu fileMenu;;

    private Label endScale;
    private Label scaleValue;
    private Label startScale;

    private FormLayout layout;

    private FormData settingsButtonData;
    private FormData scaleValueData;
    private FormData endScaleData;
    private FormData sliderBar;
    private FormData startScaleData;

    public ApplicationView() {
	init();
	application.setText("UNC SportsSync");

	settings = new Settings(60);

	application.setLayout(layout);
	//
	// // Define Options Dialog
	// //dialog = new Shell (shell);
	// dialog.setLayout (new FormLayout());
	// // Labels for INPUT and OUTPUT
	// Label dl = new Label(dialog, SWT.LEFT);
	// dl.setText("Caption");
	// FormData dld = new FormData(100, 30);
	// dld.left = new FormAttachment(50);
	// dld.bottom = new FormAttachment(100);
	// dl.setLayoutData(dld);
	// final Button ok = new Button (dialog, SWT.PUSH);
	// ok.setText ("OK");
	// Button cancel = new Button (dialog, SWT.PUSH);
	// cancel.setText ("Cancel");
	// Listener listener =new Listener () {
	// public void handleEvent (Event event) {
	// dialog.close ();
	// }
	// };
	// ok.addListener (SWT.Selection, listener);
	// cancel.addListener (SWT.Selection, listener);

	// Line-out options button
	settingsButton.setText("Settings");

	settingsButtonData.right = new FormAttachment(100);
	settingsButtonData.top = new FormAttachment(0);
	settingsButton.setLayoutData(settingsButtonData);

	// Button cancelButton = new Button(shell, SWT.PUSH);
	// cancelButton.setText("Input");
	//
	// FormData cancelData = new FormData(80, 30);
	// cancelData.right = new FormAttachment(90);
	// cancelData.bottom = new FormAttachment(95);
	// cancelButton.setLayoutData(cancelData);
	//
	// Button okButton = new Button(shell, SWT.PUSH);
	// okButton.setText("OK");
	//
	// FormData okData = new FormData(80, 30);
	// okData.right = new FormAttachment(cancelButton, -5, SWT.LEFT);
	// okData.bottom = new FormAttachment(cancelButton, 0, SWT.BOTTOM);
	// okButton.setLayoutData(okData);

	//
	// Button clearButton = new Button(shell, SWT.PUSH);
	// clearButton.setText("Clear");
	//
	// FormData clearData = new FormData(80, 30);
	// clearData.left = new FormAttachment(10);
	// clearData.bottom = new FormAttachment(95);
	// clearButton.setLayoutData(clearData);

	// Label label = new Label(shell, SWT.LEFT);
	// label.setText("Label 1");
	// FormData nameLabelData = new FormData(200, 20);
	// nameLabelData.left = new FormAttachment(10);
	// nameLabelData.top = new FormAttachment(10);

	// Create menu bar
	cascadeFileMenu.setText("&File");
	cascadeFileMenu.setMenu(fileMenu);

	// // "Input" sub menu in "File" Menu
	// MenuItem subMenuInput = new MenuItem(fileMenu, SWT.CASCADE);
	// subMenuInput.setText("Input");
	//
	// Menu inputSubmenu = new Menu(shell, SWT.DROP_DOWN);
	// subMenuInput.setMenu(inputSubmenu);
	//
	// MenuItem micIn = new MenuItem(inputSubmenu, SWT.PUSH);
	// micIn.setText("&Microphone Line-In");
	//
	// MenuItem onlineIn = new MenuItem(inputSubmenu, SWT.PUSH);
	// onlineIn.setText("&Online Radio");
	//
	// // "Output" sub menu in "File" Menu
	// MenuItem subMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
	// subMenuItem.setText("Output");
	//
	// Menu submenu = new Menu(shell, SWT.DROP_DOWN);
	// subMenuItem.setMenu(submenu);
	//
	// MenuItem jackOut = new MenuItem(submenu, SWT.PUSH);
	// jackOut.setText("&Audio Line-Out");
	//
	// MenuItem speakers = new MenuItem(submenu, SWT.PUSH);
	// speakers.setText("&Built-In Speakers");

	exitItem.setText("&Exit");
	application.setMenuBar(menuBar);

	// Slider

	// Rectangle clientArea = shell.getClientArea ();
	scale.setMaximum(1);
	scale.setMaximum(settings.delayTime * 10);
	scale.setIncrement(1);
	scale.setPageIncrement(10);

	sliderBar.left = new FormAttachment(25);
	sliderBar.bottom = new FormAttachment(50);
	scale.setLayoutData(sliderBar);

	// labels for Amount on Scale

	scaleValue.setText("Delays: 0 seconds");
	scaleValueData.left = new FormAttachment(scale, 100, SWT.LEFT);
	scaleValueData.top = new FormAttachment(scale, -50, SWT.TOP);
	scaleValue.setLayoutData(scaleValueData);

	// labels for scale

	startScale.setText("Live");
	startScaleData.left = new FormAttachment(scale, 0, SWT.LEFT);
	startScaleData.bottom = new FormAttachment(scale, 30, SWT.BOTTOM);
	startScale.setLayoutData(startScaleData);

	endScale.setText(settings.delayTime + " sec");
	endScaleData.right = new FormAttachment(scale, 20, SWT.RIGHT);
	endScaleData.bottom = new FormAttachment(scale, 30, SWT.BOTTOM);
	endScale.setLayoutData(endScaleData);

	// Slider slider = new Slider (shell, SWT.HORIZONTAL);
	// Rectangle clientArea = shell.getClientArea ();
	// slider.setBounds (clientArea.x + 10, clientArea.y + 10, 200, 32);
	//
	// /*FormData sliderBar = new FormData(400, 20);
	// sliderBar.left = new FormAttachment(30);
	// sliderBar.top = new FormAttachment(50);
	// slider.setLayoutData(sliderBar);
	// */
	//
	// slider.addListener (SWT.Selection, new Listener () {
	// public void handleEvent (Event event) {
	// String string = "SWT.NONE";
	// switch (event.detail) {
	// case SWT.DRAG: string = "SWT.DRAG"; break;
	// case SWT.HOME: string = "SWT.HOME"; break;
	// case SWT.END: string = "SWT.END"; break;
	// case SWT.ARROW_DOWN: string = "SWT.ARROW_DOWN"; break;
	// case SWT.ARROW_UP: string = "SWT.ARROW_UP"; break;
	// case SWT.PAGE_DOWN: string = "SWT.PAGE_DOWN"; break;
	// case SWT.PAGE_UP: string = "SWT.PAGE_UP"; break;
	// }
	// System.out.println ("Scroll detail -> " + string);
	// }
	// });

	// Point p = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	// label.setBounds(5, 5, p.x+5, p.y+5);

	// dialog = new Shell(shell, SWT.TITLE);
	// dialog.setText("Options...");
	// dialog.setSize(300, 300);
	// dialog.setLocation(200, 200);
	// dialog.open();
	// dialog.setVisible(false);

	// dialog.open();

	application.setSize(600, 450);
	application.setLocation(100, 100);

    }

    public void init() {
	display = new Display();
	application = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);

	layout = new FormLayout();
	settingsButton = new Button(application, SWT.PUSH);

	scaleValue = new Label(application, SWT.LEFT);
	endScale = new Label(application, SWT.LEFT);
	startScale = new Label(application, SWT.LEFT);

	settingsButtonData = new FormData(60, 25);

	menuBar = new Menu(application, SWT.BAR);
	cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
	fileMenu = new Menu(application, SWT.DROP_DOWN);
	exitItem = new MenuItem(fileMenu, SWT.PUSH);

	scale = new Scale(application, SWT.HORIZONTAL);
	scaleValueData = new FormData(150, 30);
	endScaleData = new FormData(50, 30);
	sliderBar = new FormData(300, 40);
	startScaleData = new FormData(50, 30);
    }

    public Shell getApplication() {
	return application;
    }

    public void setApplication(Shell application) {
	this.application = application;
    }

    public Settings getSettings() {
	return settings;
    }

    public void setSettings(Settings settings) {
	this.settings = settings;
    }

    public Display getDisplay() {
	return display;
    }

    public void setDisplay(Display display) {
	this.display = display;
    }

    public void open() {
	application.open();

	while (!application.isDisposed()) {
	    if (!display.readAndDispatch()) {
		display.sleep();
	    }
	}
    }

    public void addSettingsButtonListener(Listener listener) {
	settingsButton.addListener(SWT.Selection, listener);
    }

    public void addExitListener(SelectionAdapter adapter) {
	exitItem.addSelectionListener(adapter);
    }

    public void addSliderListner(Listener listener) {
	scale.addListener(SWT.Selection, listener);
    }

    public void dispose() {
	display.dispose();
    }

    public void updateDelayTime() {
	System.out.println("Input Selection detail -> " + DelayList.getText());
	settings.delayTime = (DelayList.getSelectionIndex() + 1) * 15;
	endScale.setText(settings.delayTime + " sec");
	scale.setMaximum(settings.delayTime * 10);
    }

    public void setDelayAmountText(double amount) {
	scaleValue.setText("Delay: " + amount + " seconds");
    }

    public void exit() {
	application.getDisplay().dispose();
	System.exit(0);
    }

    public Scale getScale() {
	return scale;
    }
}