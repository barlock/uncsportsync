package edu.unc.cs.sportsync.main.ui.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.ui.IView;

public class ApplicationView implements IView {

    private Shell application;
    private Settings settings;
    private Display display;
    private Scale scale;

    private Button OnOffButton;
    private FormData OnOffButtonData;

    private Scale volumeScale;
    private FormData volumeScaleData;
    private Button muteButton;
    private FormData muteButtonData;
    private Label volumeLabel;
    private FormData volumeLabelData;

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

    public ApplicationView(Settings settings) {
        init();
        application.setText("UNC SportsSync");

        this.settings = settings;

        application.setLayout(layout);

        // Line-out options button
        settingsButton.setText("Settings");

        settingsButtonData.right = new FormAttachment(100);
        settingsButtonData.top = new FormAttachment(0);
        settingsButton.setLayoutData(settingsButtonData);

        // Create menu bar
        cascadeFileMenu.setText("&File");
        cascadeFileMenu.setMenu(fileMenu);

        exitItem.setText("&Exit");
        application.setMenuBar(menuBar);

        // Volume control
        volumeScaleData.top = new FormAttachment(70);
        volumeScaleData.left = new FormAttachment(10);
        volumeScale.setLayoutData(volumeScaleData);
        volumeScale.setMinimum(0);
        volumeScale.setMaximum(100);
        volumeScale.setIncrement(1);
        volumeScale.setPageIncrement(10);

        muteButtonData.top = new FormAttachment(volumeScale, 30, SWT.TOP);
        muteButtonData.left = new FormAttachment(volumeScale, 60, SWT.LEFT);
        muteButton.setLayoutData(muteButtonData);
        // Image speakerImg = new Image(display,
        // "C:\\Users\\waivers\\SSworkspace\\SportSync\\src\\edu\\unc\\cs\\sportsync\\main\\ui\\application\\speaker.png");
        // muteButton.setBackgroundImage(speakerImg);
        muteButton.setText("Mute");

        volumeLabelData.top = new FormAttachment(volumeScale, -40, SWT.TOP);
        volumeLabelData.left = new FormAttachment(volumeScale, 0, SWT.LEFT);
        volumeLabel.setLayoutData(volumeLabelData);
        volumeLabel.setText("Volume Control");

        // On/Off Button
        OnOffButtonData.top = new FormAttachment(20);
        OnOffButtonData.left = new FormAttachment(25);
        OnOffButton.setLayoutData(OnOffButtonData);
        OnOffButton.setText("ON/Off");

        // Slider

        // Rectangle clientArea = shell.getClientArea ();
        scale.setMinimum(1);
        scale.setMaximum(settings.getDelayTime() * 10);
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

        endScale.setText(settings.getDelayTime() + " sec");
        endScaleData.right = new FormAttachment(scale, 20, SWT.RIGHT);
        endScaleData.bottom = new FormAttachment(scale, 30, SWT.BOTTOM);
        endScale.setLayoutData(endScaleData);

        application.setSize(600, 450);
        application.setLocation(100, 100);

    }

    public void addExitListener(SelectionAdapter adapter) {
        exitItem.addSelectionListener(adapter);
    }

    public void addMuteButtonListener(Listener listener) {
        muteButton.addListener(SWT.Selection, listener);
    }

    public void addOnOffButtonListener(Listener listener) {
        OnOffButton.addListener(SWT.Selection, listener);
    }

    public void addSettingsButtonListener(Listener listener) {
        settingsButton.addListener(SWT.Selection, listener);
    }

    public void addSliderListener(Listener listener) {
        scale.addListener(SWT.Selection, listener);
    }

    public void addVolumeSliderListener(Listener listener) {
        volumeScale.addListener(SWT.Selection, listener);
    }

    public void dispose() {
        display.dispose();
    }

    public void exit() {
        application.getDisplay().dispose();
        System.exit(0);
    }

    public Shell getApplication() {
        return application;
    }

    public Display getDisplay() {
        return display;
    }

    public Scale getScale() {
        return scale;
    }

    public Settings getSettings() {
        return settings;
    }

    public Scale getVolumeScale() {
        return volumeScale;
    }

    public void init() {
        display = new Display();
        application = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);

        layout = new FormLayout();
        settingsButton = new Button(application, SWT.PUSH);

        OnOffButton = new Button(application, SWT.TOGGLE);
        OnOffButtonData = new FormData(70, 30);

        muteButton = new Button(application, SWT.TOGGLE);
        muteButtonData = new FormData(50, 30);
        volumeScale = new Scale(application, SWT.VERTICAL);
        volumeScaleData = new FormData(40, 100);
        volumeLabel = new Label(application, SWT.VERTICAL);
        volumeLabelData = new FormData(100, 40);

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

    @Override
    public void open() {
        application.open();

        while (!application.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public void setApplication(Shell application) {
        this.application = application;
    }

    public void setDelayAmountText(double amount) {
        scaleValue.setText("Delay: " + amount + " seconds");
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void updateDelayTime() {
        endScale.setText(settings.getDelayTime() + " sec");
        scale.setMaximum(settings.getDelayTime() * 10);
    }
}