package edu.unc.cs.sportsync.main.ui.settings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.ui.IView;

public class SettingsDialogView implements IView {
    private final Settings settings;
    private final Shell settingsDialog;

    private final FormLayout layout;

    private Combo inputList;
    private FormData inputListForm;
    private Label inputLabel;
    private FormData inputLabelForm;

    private Combo outputList;
    private FormData outputListForm;
    private Label outputLabel;
    private FormData outputLabelForm;

    private Combo delayList;
    private FormData delayListForm;
    private Label delayLabel;
    private FormData delayLabelForm;

    private Button okButton;
    private FormData okButtonForm;

    public SettingsDialogView(Shell shell, Settings s) {
        settings = s;

        // some quirk of SWT prevents this from going into the init function
        settingsDialog = new Shell(shell);
        layout = new FormLayout();

        settingsDialog.setText("Settings");
        settingsDialog.setSize(400, 500);
        settingsDialog.setLocation(400, 150);
        settingsDialog.setLayout(layout);

        init();

        // INPUT and OUTPUT selection
        // inputList.select(0);
        inputListForm.left = new FormAttachment(50);
        inputListForm.top = new FormAttachment(20);
        inputList.setLayoutData(inputListForm);

        // outputList.select(0);
        outputListForm.left = new FormAttachment(inputList, 0, SWT.LEFT);
        outputListForm.bottom = new FormAttachment(inputList, 50, SWT.BOTTOM);
        outputList.setLayoutData(outputListForm);

        // Labels for INPUT and OUTPUT
        inputLabel.setText("Audio Input:");
        inputLabelForm.right = new FormAttachment(inputList, -10, SWT.LEFT);
        inputLabelForm.bottom = new FormAttachment(inputList, 0, SWT.BOTTOM);
        inputLabel.setLayoutData(inputLabelForm);

        outputLabel.setText("Audio Output:");
        outputLabelForm.right = new FormAttachment(outputList, -10, SWT.LEFT);
        outputLabelForm.bottom = new FormAttachment(outputList, 0, SWT.BOTTOM);
        outputLabel.setLayoutData(outputLabelForm);

        // Optional Delay times

        delayList.select(settings.delayTime / 15 - 1);
        delayListForm.left = new FormAttachment(inputList, 0, SWT.LEFT);
        delayListForm.bottom = new FormAttachment(inputList, 100, SWT.BOTTOM);
        delayList.setLayoutData(delayListForm);

        delayLabel.setText("Audio Delay Time:");
        delayLabelForm.right = new FormAttachment(delayList, -10, SWT.LEFT);
        delayLabelForm.bottom = new FormAttachment(delayList, 0, SWT.BOTTOM);
        delayLabel.setLayoutData(delayLabelForm);

        // Okay/Exit Button
        okButton.setText("OK");
        okButtonForm.bottom = new FormAttachment(95);
        okButtonForm.left = new FormAttachment(45);
        okButton.setLayoutData(okButtonForm);

    }

    public void addDelayTimeChangeListener(Listener listener) {
        delayList.addListener(SWT.Selection, listener);
    }

    public void addInputSelectionListener(Listener listener) {
        inputList.addListener(SWT.Selection, listener);
    }

    public void addOkButtonListener(Listener listener) {
        okButton.addListener(SWT.Selection, listener);
    }

    public void addOutputSelectionListener(Listener listener) {
        outputList.addListener(SWT.Selection, listener);
    }

    public void close() {
        settingsDialog.close();
    }

    public Combo getInputList() {
        return inputList;
    }

    public Combo getOutputList() {
        return outputList;
    }

    public void init() {
        inputList = new Combo(settingsDialog, SWT.READ_ONLY);
        inputListForm = new FormData(100, 100);

        outputList = new Combo(settingsDialog, SWT.READ_ONLY);
        outputListForm = new FormData(100, 100);

        inputLabel = new Label(settingsDialog, SWT.RIGHT);
        inputLabelForm = new FormData(100, 20);

        outputLabel = new Label(settingsDialog, SWT.RIGHT);
        outputLabelForm = new FormData(100, 20);

        delayList = new Combo(settingsDialog, SWT.READ_ONLY);
        delayListForm = new FormData(100, 100);

        delayLabel = new Label(settingsDialog, SWT.RIGHT);
        delayLabelForm = new FormData(100, 20);

        okButton = new Button(settingsDialog, SWT.PUSH);
        okButtonForm = new FormData(50, 30);
    }

    public void open() {
        settingsDialog.open();
    }

    public void setDelayListTimes(String[] delayTimes) {
        delayList.setItems(delayTimes);
        delayList.select(settings.delayTime / 15 - 1);
    }

    public void setInputDeviceNames(String[] inputNames) {
        inputList.setItems(inputNames);
        inputList.select(0);
    }

    public void setOutputDeviceNames(String[] outputNames) {
        outputList.setItems(outputNames);
        outputList.select(0);
    }
}
