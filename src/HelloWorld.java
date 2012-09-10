import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class HelloWorld {

	private Shell shell;

	public HelloWorld(Display display) {

		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN );
		shell.setText("UNC SportsSync&amp");

		initUI();

		shell.setSize(600, 450);
		shell.setLocation(100, 100);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void initUI() {
		FormLayout layout = new FormLayout();
		shell.setLayout(layout);

		Button okButton = new Button(shell, SWT.PUSH);
		okButton.setText("OK");

		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.setText("Cancel");

		FormData cancelData = new FormData(80, 30);
		cancelData.right = new FormAttachment(90);
		cancelData.bottom = new FormAttachment(95);
		cancelButton.setLayoutData(cancelData);

		FormData okData = new FormData(80, 30);
		okData.right = new FormAttachment(cancelButton, -5, SWT.LEFT);
		okData.bottom = new FormAttachment(cancelButton, 0, SWT.BOTTOM);
		okButton.setLayoutData(okData);

		Button clearButton = new Button(shell, SWT.PUSH);
		clearButton.setText("Clear");

		FormData clearData = new FormData(80, 30);
		clearData.left = new FormAttachment(10);
		clearData.bottom = new FormAttachment(95);
		clearButton.setLayoutData(clearData);

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

		// "Input" sub menu in "File" Menu
		MenuItem subMenuInput = new MenuItem(fileMenu, SWT.CASCADE);
		subMenuInput.setText("Input");

		Menu inputSubmenu = new Menu(shell, SWT.DROP_DOWN);
		subMenuInput.setMenu(inputSubmenu);

		MenuItem micIn = new MenuItem(inputSubmenu, SWT.PUSH);
		micIn.setText("&Microphone Line-In");

		MenuItem onlineIn = new MenuItem(inputSubmenu, SWT.PUSH);
		onlineIn.setText("&Online Radio");

		// "Output" sub menu in "File" Menu
		MenuItem subMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
		subMenuItem.setText("Output");

		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		subMenuItem.setMenu(submenu);

		MenuItem jackOut = new MenuItem(submenu, SWT.PUSH);
		jackOut.setText("&Audio Line-Out");

		MenuItem speakers = new MenuItem(submenu, SWT.PUSH);
		speakers.setText("&Built-In Speakers");

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
		
		// labels for Amount on Scale
		final Label scaleValue = new Label(shell, SWT.LEFT);
		scaleValue.setText("Delays: 0 seconds");
		FormData scaleValueData = new FormData(150, 30);
		scaleValueData.left = new FormAttachment(40);
		scaleValueData.top = new FormAttachment(30);
		scaleValue.setLayoutData(scaleValueData);

		// Slider
		final Scale scale = new Scale(shell, SWT.HORIZONTAL);  // WHY FINAL???
		// Rectangle clientArea = shell.getClientArea ();
		scale.setMaximum(1);
		scale.setMaximum(60);
		scale.setIncrement(1);
		scale.setPageIncrement(5);

		FormData sliderBar = new FormData(300, 40);
		sliderBar.left = new FormAttachment(25);
		sliderBar.top = new FormAttachment(50);
		scale.setLayoutData(sliderBar);

		scale.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Scroll detail -> " + scale.getSelection());
				scaleValue.setText("Delay: "+scale.getSelection()+ " seconds");
				scaleValue.forceFocus();
			}
		});
		
		// labels for scale
		Label startScale = new Label(shell, SWT.LEFT);
		startScale.setText("0 sec");
		FormData startScaleData = new FormData(50, 30);
		startScaleData.left = new FormAttachment(25);
		startScaleData.top = new FormAttachment(60);
		startScale.setLayoutData(startScaleData);
		
		Label endScale = new Label(shell, SWT.LEFT);
		endScale.setText("60 sec");
		FormData endScaleData = new FormData(50, 30);
		endScaleData.left = new FormAttachment(71);
		endScaleData.top = new FormAttachment(60);
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
		new HelloWorld(display);
		display.dispose();
	}
}