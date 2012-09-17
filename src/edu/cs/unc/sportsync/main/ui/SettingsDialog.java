package edu.cs.unc.sportsync.main.ui;

import java.util.ArrayList;
import java.util.Collection;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import edu.cs.unc.sportsync.main.settings.Settings;
import edu.cs.unc.sportsync.main.sound.AudioControl;

public class SettingsDialog {
	
	Combo inputList, OutputList, DelayList;
	Settings settings;
	Shell dialog;

	public SettingsDialog(Shell shell, Settings s, Listener delayTimeChangeListener) {
		dialog = new Shell(shell);
		settings = s;
		
		dialog.setText("Settings");
		dialog.setSize(400, 500);
		dialog.setLocation(400, 150);

		FormLayout layout = new FormLayout();
		dialog.setLayout(layout);

		// INPUT and OUTPUT selection
		inputList = new Combo(dialog, SWT.READ_ONLY);
		inputList.setItems(getInputDeviceNames());
		inputList.select(0);
		FormData inputListF = new FormData(100, 100);
		inputListF.left = new FormAttachment(50);
		inputListF.top = new FormAttachment(20);
		inputList.setLayoutData(inputListF);

		inputList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Input Selection detail -> " + inputList.getText());
			}
		});

		OutputList = new Combo(dialog, SWT.READ_ONLY);
		OutputList.setItems(getOutputDeviceNames());
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
		DelayList.select(settings.delayTime/15-1);
		FormData DelayListF = new FormData(100, 100);
		DelayListF.left = new FormAttachment(inputList, 0, SWT.LEFT);
		DelayListF.bottom = new FormAttachment(inputList, 100, SWT.BOTTOM);
		DelayList.setLayoutData(DelayListF);

		DelayList.addListener(SWT.Selection, delayTimeChangeListener);
		
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

	public void open() {
		dialog.open();
	}
	
	private String[] getInputDeviceNames() {
		ArrayList<Info> inputDevices = new ArrayList<Info>(AudioControl.getInputDevices());
		String[] names = new String[inputDevices.size()];
		
		for(int i = 0; i < inputDevices.size(); i++) {
			names[i] = inputDevices.get(i).getName();
		}
		
		return names;
	}
	
	private String[] getOutputDeviceNames() {
		ArrayList<Info> outputDevices = new ArrayList<Info>(AudioControl.getOutputDevices());
		String[] names = new String[outputDevices.size()];
		
		for(int i = 0; i < outputDevices.size(); i++) {
			names[i] = outputDevices.get(i).getName();
		}
		
		return names;
	}
}
