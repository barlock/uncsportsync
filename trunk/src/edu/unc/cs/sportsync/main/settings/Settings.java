package edu.unc.cs.sportsync.main.settings;

import javax.sound.sampled.Mixer;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.unc.cs.sportsync.main.sound.AudioControl;

import java.io.File;
import java.io.IOException;

public class Settings {
    private final String PATH = "settings.xml";
    private int delayTime;

    private Mixer.Info inputMixer;
    private Mixer.Info outputMixer;

    public Settings() {
        this.delayTime = 60;
        initSettings();
    }
    
    private void initSettings() {
        File file = new File(PATH);
        boolean failed = false;
        
        if(file.exists()) {    
            failed = !parseSettingsFile(file); 
        }
        
        if(!file.exists() || failed) {
            delayTime = 60;
            inputMixer = AudioControl.getInputDevices().iterator().next();
            outputMixer = AudioControl.getOutputDevices().iterator().next();
        }
        
    }

    private boolean parseSettingsFile(File file) {
        try {
            SettingsXmlParser parser = new SettingsXmlParser(file);
            delayTime = Integer.valueOf(parser.getDelayTime());
            inputMixer = parser.getInputMixer();
            outputMixer = parser.getOutputMixer();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } 
        
        return true;
    }

    public int getDelayTime() { 
        return delayTime;
    }

    public Mixer.Info getInputMixer() {
        return inputMixer;
    }

    public Mixer.Info getOutputMixer() {
        return outputMixer;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void setInputMixer(Mixer.Info inputMixer) {
        this.inputMixer = inputMixer;
    }

    public void setOutputMixer(Mixer.Info outputMixer) {
        this.outputMixer = outputMixer;
    }
    
    public void save() {
        SettingsXmlBuilder builder = new SettingsXmlBuilder();

        builder.addDelayTime(delayTime);
        builder.addInputMixer(inputMixer);
        builder.addOutputMixer(outputMixer);

        builder.saveTo(PATH);
    }

}
