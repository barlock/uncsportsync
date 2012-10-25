package edu.unc.cs.sportsync.main.ui.settings;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.xwt.DefaultLoadingContext;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.annotation.UI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolItem;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.sound.AudioControl;

public class SettingsDialog extends Composite {

	private final StackLayout settingsBoxLayout;

	@UI
	Composite settingsBox;

	@UI
	AudioSettingsTab audioSettingsTab;

	@UI
	AboutPageTab aboutPageTab;

	@UI
	HelpPageTab helpPageTab;

	@UI
	Button saveButton;

	@UI
	ToolItem audioButton;

	@UI
	ToolItem helpButton;

	@UI
	ToolItem aboutButton;

	public SettingsDialog(Composite parent, int style, Settings settings, Listener applyButtonListener, AudioControl audioControl) {
		super(parent, style);
		setLayout(new FillLayout());
		// load XWT
		String name = SettingsDialog.class.getSimpleName() + IConstants.XWT_EXTENSION_SUFFIX;
		try {
			URL url = SettingsDialog.class.getResource(name);
			Map<String, Object> options = new HashMap<String, Object>();
			options.put(IXWTLoader.CLASS_PROPERTY, this);
			options.put(IXWTLoader.CONTAINER_PROPERTY, this);
			XWT.setLoadingContext(new DefaultLoadingContext(this.getClass().getClassLoader()));
			XWT.loadWithOptions(url, options);
		} catch (Throwable e) {
			throw new Error("Unable to load " + name, e);
		}
		saveButton.addListener(SWT.Selection, applyButtonListener);
		audioSettingsTab.setSettings(settings);
		audioSettingsTab.setAudioControl(audioControl);
		settingsBoxLayout = (StackLayout) settingsBox.getLayout();

		settingsBoxLayout.topControl = audioSettingsTab;
		settingsBox.layout();

		audioButton.setSelection(true);
	}

	public boolean hasMaxDelayChanged() {
		return audioSettingsTab.hasMaxDelaySpinnerChanged();
	}

	public void onCancelButtonSelection(Event event) {
		getShell().close();
	}

	public void onAboutButtonSelection(Event event) {
		settingsBoxLayout.topControl = aboutPageTab;
		settingsBox.layout();
	}

	public void onAudioButtonSelection(Event event) {
		settingsBoxLayout.topControl = audioSettingsTab;
		settingsBox.layout();
	}

	public void onHelpButtonSelection(Event event) {
		settingsBoxLayout.topControl = helpPageTab;
		settingsBox.layout();
	}

	public void updateSettings() {
		audioSettingsTab.updateSettings();
	}
}
