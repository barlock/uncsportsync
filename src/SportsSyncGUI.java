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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class SportsSyncGUI {

	private Shell shell;
	private Shell dialog;
	private Scale scale;
	Combo combo1, combo2;

	public SportsSyncGUI(Display display) {

		
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN );
		shell.setText("UNC SportsSync");
		initUI();
		
//		optionsShell = new Shell(shell, SWT.TITLE);
//		optionsShell.setText("Options...");
//		optionsShell.setSize(300, 300);
//		optionsShell.setLocation(200, 200);
//		optionsShell.open();
//		optionsShell.setVisible(false);

		dialog.setSize(250,150);
		
		shell.setSize(600, 450);
		shell.setLocation(100, 100);

		shell.open();
		
		while (!dialog.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void initUI() {
		FormLayout layout = new FormLayout();
		shell.setLayout(layout);
		
		// Define Options Dialog
		dialog = new Shell (shell);
		dialog.setLayout (new FormLayout());
		final Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("OK");
		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setText ("Cancel");
		Listener listener =new Listener () {
			public void handleEvent (Event event) {
				dialog.close ();
			}
		};
		ok.addListener (SWT.Selection, listener);
		cancel.addListener (SWT.Selection, listener);
		
		// INPUT and OUTPUT selection		
		combo2 = new Combo (shell, SWT.READ_ONLY);
		combo2.setItems (new String [] {"Line-Out Jack", "Speakers"});
		combo2.select(0);
		FormData combo2F = new FormData(100, 100);
		combo2F.left = new FormAttachment(70);
		combo2F.bottom = new FormAttachment(85);
		combo2.setLayoutData(combo2F);
		
		combo2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Input Selection detail -> " + combo2.getText());
				dialog.pack ();
				dialog.open ();
				
			}
		});
		
		combo1 = new Combo (shell, SWT.READ_ONLY);
		combo1.setItems (new String [] {"Line-In", "Online Radio"});
		combo1.select(0);
		FormData combo1F = new FormData(100, 100);
		combo1F.left = new FormAttachment(10);
		combo1F.bottom = new FormAttachment(85);
		combo1.setLayoutData(combo1F);
		
		combo1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Input Selection detail -> " + combo1.getText());
			}
		});
		
		// Labels for INPUT and OUTPUT
		Label ln = new Label(shell, SWT.LEFT);
		ln.setText("Audio Input:");
		FormData lnd = new FormData(100, 30);
		lnd.left = new FormAttachment(combo1, 0, SWT.LEFT);
		lnd.bottom = new FormAttachment(combo1, -20, SWT.BOTTOM);
		ln.setLayoutData(lnd);
		
		Label lo = new Label(shell, SWT.LEFT);
		lo.setText("Audio Output:");
		FormData lod = new FormData(100, 30);
		lod.left = new FormAttachment(combo2, 0, SWT.LEFT);
		lod.bottom = new FormAttachment(combo2, -20, SWT.BOTTOM);
		lo.setLayoutData(lod);
		
		//LALALA new comment!
		
		// Line-in options button
		Button inOption = new Button(shell, SWT.PUSH);
		inOption.setText("Options...");
		FormData inOptionData = new FormData(60, 25);
		inOptionData.left = new FormAttachment(combo1, 5, SWT.LEFT);
		inOptionData.bottom = new FormAttachment(combo1, 40, SWT.BOTTOM);
		inOption.setLayoutData(inOptionData);
		
		// Line-out options button
		Button outOption = new Button(shell, SWT.PUSH);
		outOption.setText("Options...");
		FormData outOptionData = new FormData(60, 25);
		outOptionData.left = new FormAttachment(combo2, 5, SWT.LEFT);
		outOptionData.bottom = new FormAttachment(combo2, 40, SWT.BOTTOM);
		outOption.setLayoutData(outOptionData);

//		Button cancelButton = new Button(shell, SWT.PUSH);
//		cancelButton.setText("Input");
//
//		FormData cancelData = new FormData(80, 30);
//		cancelData.right = new FormAttachment(90);
//		cancelData.bottom = new FormAttachment(95);
//		cancelButton.setLayoutData(cancelData);
//
//		Button okButton = new Button(shell, SWT.PUSH);
//		okButton.setText("OK");
//		
//		FormData okData = new FormData(80, 30);
//		okData.right = new FormAttachment(cancelButton, -5, SWT.LEFT);
//		okData.bottom = new FormAttachment(cancelButton, 0, SWT.BOTTOM);
//		okButton.setLayoutData(okData);
		
//
//		Button clearButton = new Button(shell, SWT.PUSH);
//		clearButton.setText("Clear");
//
//		FormData clearData = new FormData(80, 30);
//		clearData.left = new FormAttachment(10);
//		clearData.bottom = new FormAttachment(95);
//		clearButton.setLayoutData(clearData);

//		Label label = new Label(shell, SWT.LEFT);
//		label.setText("Label 1");
//		FormData nameLabelData = new FormData(200, 20);
//		nameLabelData.left = new FormAttachment(10);
//		nameLabelData.top = new FormAttachment(10);

		// Create menu bar
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeFileMenu.setText("&File");

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu.setMenu(fileMenu);

//		// "Input" sub menu in "File" Menu
//		MenuItem subMenuInput = new MenuItem(fileMenu, SWT.CASCADE);
//		subMenuInput.setText("Input");
//
//		Menu inputSubmenu = new Menu(shell, SWT.DROP_DOWN);
//		subMenuInput.setMenu(inputSubmenu);
//
//		MenuItem micIn = new MenuItem(inputSubmenu, SWT.PUSH);
//		micIn.setText("&Microphone Line-In");
//
//		MenuItem onlineIn = new MenuItem(inputSubmenu, SWT.PUSH);
//		onlineIn.setText("&Online Radio");
//
//		// "Output" sub menu in "File" Menu
//		MenuItem subMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
//		subMenuItem.setText("Output");
//
//		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
//		subMenuItem.setMenu(submenu);
//
//		MenuItem jackOut = new MenuItem(submenu, SWT.PUSH);
//		jackOut.setText("&Audio Line-Out");
//
//		MenuItem speakers = new MenuItem(submenu, SWT.PUSH);
//		speakers.setText("&Built-In Speakers");

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
		scale = new Scale(shell, SWT.HORIZONTAL);  // WHY FINAL???
		// Rectangle clientArea = shell.getClientArea ();
		scale.setMaximum(1);
		scale.setMaximum(600);
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
				System.out.println("Scroll detail -> " + scale.getSelection()/10.0);
				scaleValue.setText("Delay: "+scale.getSelection()/10.0+ " seconds");
				scaleValue.forceFocus();
			}
		});
		
		// labels for scale
		Label startScale = new Label(shell, SWT.LEFT);
		startScale.setText("0 sec");
		FormData startScaleData = new FormData(50, 30);
		startScaleData.left = new FormAttachment(scale, 0, SWT.LEFT);
		startScaleData.bottom = new FormAttachment(scale, 30, SWT.BOTTOM);
		startScale.setLayoutData(startScaleData);
		
		Label endScale = new Label(shell, SWT.LEFT);
		endScale.setText("60 sec");
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

	public static void main(String[] args) {
		Display display = new Display();
		new SportsSyncGUI(display);
		display.dispose();
	}
}