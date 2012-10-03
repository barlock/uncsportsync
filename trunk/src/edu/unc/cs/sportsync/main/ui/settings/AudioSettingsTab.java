package edu.unc.cs.sportsync.main.ui.settings;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import org.eclipse.e4.xwt.DefaultLoadingContext;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.annotation.UI;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

import edu.unc.cs.sportsync.main.settings.Settings;
import edu.unc.cs.sportsync.main.sound.AudioControl;

public class AudioSettingsTab extends Composite {
    
    private ArrayList<Mixer.Info> inputMixerInfo;
    private ArrayList<Mixer.Info> outputMixerInfo;
    private Settings settings;
    
    private final int MAX_DELAY = 120;
    
    @UI
    Combo inputDeviceCombo;
    
    @UI
    Combo outputDeviceCombo;
    
    @UI
    Spinner maxDelaySpinner;

    public AudioSettingsTab(Composite parent, int style) {
        super(parent, style);
        setLayout(new FillLayout());
        
        // load XWT
        String name = AudioSettingsTab.class.getSimpleName() + IConstants.XWT_EXTENSION_SUFFIX;
        try {
            URL url = AudioSettingsTab.class.getResource(name);
            Map<String, Object> options = new HashMap<String, Object>();
            options.put(IXWTLoader.CLASS_PROPERTY, this);
            options.put(IXWTLoader.CONTAINER_PROPERTY, this);
            XWT.setLoadingContext(new DefaultLoadingContext(this.getClass().getClassLoader()));
            XWT.loadWithOptions(url, options);
        } catch (Throwable e) {
            throw new Error("Unable to load " + name, e);
        }
    }
    
    public void updateSettings() {
        settings.setDelayTime(maxDelaySpinner.getSelection());
        settings.setInputMixer(inputMixerInfo.get(inputDeviceCombo.getSelectionIndex()));
        settings.setOutputMixer(outputMixerInfo.get(outputDeviceCombo.getSelectionIndex()));
    }
    
    private String[] getInputDeviceNames() {
        inputMixerInfo = new ArrayList<Info>(AudioControl.getInputDevices());
        String[] names = new String[inputMixerInfo.size()];

        for (int i = 0; i < inputMixerInfo.size(); i++) {
            names[i] = inputMixerInfo.get(i).getName();
        }

        return names;
    }

    private String[] getOutputDeviceNames() {
        outputMixerInfo = new ArrayList<Info>(AudioControl.getOutputDevices());
        String[] names = new String[outputMixerInfo.size()];

        for (int i = 0; i < outputMixerInfo.size(); i++) {
            names[i] = outputMixerInfo.get(i).getName();
        }

        return names;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        
        inputDeviceCombo.setItems(getInputDeviceNames());
        inputDeviceCombo.select(inputMixerInfo.indexOf(settings.getInputMixer()));
        
        outputDeviceCombo.setItems(getOutputDeviceNames());
        outputDeviceCombo.select(outputMixerInfo.indexOf(settings.getOutputMixer()));
        
        maxDelaySpinner.setMaximum(MAX_DELAY);
        maxDelaySpinner.setSelection(settings.getDelayTime());
    }

}