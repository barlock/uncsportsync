package edu.cs.unc.sportsync.main.ui;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class Application {

	private Shell shell;
	private Shell dialog;
	private int delayTime;  // in seconds
	

	public Application(Display display) {

		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setText("UNC SportsSync");
		
		delayTime = 60;

		initMainUI();

		// dialog = new Shell(shell, SWT.TITLE);
		// dialog.setText("Options...");
		// dialog.setSize(300, 300);
		// dialog.setLocation(200, 200);
		// dialog.open();
		// dialog.setVisible(false);

		// dialog.open();

		shell.setSize(600, 450);
		shell.setLocation(100, 100);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	

	private Scale scale;
	private Label endScale;

	public void initMainUI() {
		FormLayout layout = new FormLayout();
		shell.setLayout(layout);
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
		Button settingsButton = new Button(shell, SWT.PUSH);
		settingsButton.setText("Settings");
		FormData settingsButtonData = new FormData(60, 25);
		settingsButtonData.right = new FormAttachment(100);
		settingsButtonData.top = new FormAttachment(0);
		settingsButton.setLayoutData(settingsButtonData);

		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				initSettingsUI();
				dialog.open();
			}
		};

		settingsButton.addListener(SWT.Selection, listener);

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
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeFileMenu.setText("&File");

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
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

		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText("&Exit");
		shell.setMenuBar(menuBar);

		exitItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.getDisplay().dispose();
				System.exit(0);
			}
		});

		// Slider
		scale = new Scale(shell, SWT.HORIZONTAL); // WHY FINAL???
		// Rectangle clientArea = shell.getClientArea ();
		scale.setMaximum(1);
		scale.setMaximum(delayTime*10);
		scale.setIncrement(1);
		scale.setPageIncrement(10);
		FormData sliderBar = new FormData(300, 40);
		sliderBar.left = new FormAttachment(25);
		sliderBar.bottom = new FormAttachment(50);
		scale.setLayoutData(sliderBar);

		// labels for Amount on Scale
		final Label scaleValue = new Label(shell, SWT.LEFT);
		scaleValue.setText("Delays: 0 seconds");
		FormData scaleValueData = new FormData(150, 30);
		scaleValueData.left = new FormAttachment(scale, 100, SWT.LEFT);
		scaleValueData.top = new FormAttachment(scale, -50, SWT.TOP);
		scaleValue.setLayoutData(scaleValueData);

		scale.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Scroll detail -> " + scale.getSelection()
						/ 10.0);
				scaleValue.setText("Delay: " + scale.getSelection() / 10.0
						+ " seconds");
				scaleValue.forceFocus();
			}
		});

		// labels for scale
		Label startScale = new Label(shell, SWT.LEFT);
		startScale.setText("Live");
		FormData startScaleData = new FormData(50, 30);
		startScaleData.left = new FormAttachment(scale, 0, SWT.LEFT);
		startScaleData.bottom = new FormAttachment(scale, 30, SWT.BOTTOM);
		startScale.setLayoutData(startScaleData);

		endScale = new Label(shell, SWT.LEFT);
		endScale.setText(delayTime + " sec");
		FormData endScaleData = new FormData(50, 30);
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
	}

	Combo inputList, OutputList, DelayList;

	public void initSettingsUI() {

		//dialog = new Shell(shell, SWT.APPLICATION_MODAL);
		dialog = new Shell(shell);
		dialog.setText("Settings");
		dialog.setSize(400, 500);
		dialog.setLocation(400, 150);

		FormLayout layout = new FormLayout();
		dialog.setLayout(layout);

		// INPUT and OUTPUT selection
		inputList = new Combo(dialog, SWT.READ_ONLY);
		String[] listOfInputDevices = new String[] { "Line-in", "Online Radio" };
		inputList.setItems(listOfInputDevices);
		inputList.select(0);
		FormData inputListF = new FormData(100, 100);
		inputListF.left = new FormAttachment(50);
		inputListF.top = new FormAttachment(20);
		inputList.setLayoutData(inputListF);

		inputList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Input Selection detail -> "
						+ inputList.getText());
			}
		});

		OutputList = new Combo(dialog, SWT.READ_ONLY);
		String[] listOfOutputDevices = new String[] { "Line-Out Jack",
				"Speakers" };
		OutputList.setItems(listOfOutputDevices);
		OutputList.select(0);
		FormData OutputListF = new FormData(100, 100);
		OutputListF.left = new FormAttachment(inputList, 0, SWT.LEFT);
		OutputListF.bottom = new FormAttachment(inputList, 50, SWT.BOTTOM);
		OutputList.setLayoutData(OutputListF);

		OutputList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Input Selection detail -> "
						+ OutputList.getText());
			}
		});

		// Labels for INPUT and OUTPUT
		Label ln = new Label(dialog, SWT.RIGHT);
		ln.setText("Audio Input:");
		FormData lnd = new FormData(100, 20);
		lnd.right = new FormAttachment(inputList, -10, SWT.LEFT);
		lnd.bottom = new FormAttachment(inputList, 0, SWT.BOTTOM);
		ln.setLayoutData(lnd);

		Label lo = new Label(dialog, SWT.RIGHT);
		lo.setText("Audio Output:");
		FormData lod = new FormData(100, 20);
		lod.right = new FormAttachment(OutputList, -10, SWT.LEFT);
		lod.bottom = new FormAttachment(OutputList, 0, SWT.BOTTOM);
		lo.setLayoutData(lod);
		
		// Optional Delay times
		DelayList = new Combo(dialog, SWT.READ_ONLY);
		String[] listOfDelayTimes = new String[40];
		String time;
		for (int i = 0 ; i < 40 ; i++ ){
			time = (i+1)/4==0 ? String.format("%d sec", 15*((i+1)%4)) :
				(15*((i+1)%4)==0 ? String.format("%d min", (i+1)/4) :
					String.format("%d min & %d sec", (i+1)/4,  15*((i+1)%4)));
			listOfDelayTimes[i] =  time;
		}
		DelayList.setItems(listOfDelayTimes);
		DelayList.select(delayTime/15-1);
		FormData DelayListF = new FormData(100, 100);
		DelayListF.left = new FormAttachment(inputList, 0, SWT.LEFT);
		DelayListF.bottom = new FormAttachment(inputList, 100, SWT.BOTTOM);
		DelayList.setLayoutData(DelayListF);

		DelayList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Input Selection detail -> "
						+ DelayList.getText());
				delayTime = (DelayList.getSelectionIndex()+1)*15;
				endScale.setText(delayTime + " sec");
				scale.setMaximum(delayTime*10);
			}
		});
		
		Label dl = new Label(dialog, SWT.RIGHT);
		dl.setText("Audio Delay Time:");
		FormData dld = new FormData(100, 20);
		dld.right = new FormAttachment(DelayList, -10, SWT.LEFT);
		dld.bottom = new FormAttachment(DelayList, 0, SWT.BOTTOM);
		dl.setLayoutData(dld);

		
		// Okay/Exit Button
		Button ok = new Button(dialog, SWT.PUSH);
		ok.setText("OK");
		FormData okForm = new FormData(50, 30);
		okForm.bottom = new FormAttachment(95);
		okForm.left = new FormAttachment(45);
		ok.setLayoutData(okForm);
		Listener listenerExit = new Listener() {
			public void handleEvent(Event event) {
				dialog.close();
			}
		};
		ok.addListener(SWT.Selection, listenerExit);
		
		

	}
}