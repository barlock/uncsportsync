package edu.unc.cs.sportsync.main.settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.Mixer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.unc.cs.sportsync.main.sound.AudioControl;

public class SettingsXmlParser {

    private Document doc;
    private Element root;

    public SettingsXmlParser(File file) throws ParserConfigurationException, SAXException, IOException {
        File fXmlFile = file;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        dBuilder = dbFactory.newDocumentBuilder();

        doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        root = doc.getDocumentElement();
    }

    public String getDelayTime() {
        return getTagValue("delayTime", root);
    }
    
    public Mixer.Info getInputMixer() {
        Element input = (Element) doc.getElementsByTagName("inputMixer").item(0);
        String name = getTagValue("name", input);
        String description = getTagValue("description", input);
        String vendor = getTagValue("vendor", input);
        String version = getTagValue("version", input);
        ArrayList<Mixer.Info> mixers = new ArrayList<Mixer.Info>(AudioControl.getInputDevices());
        
        for(Mixer.Info mixer: mixers) {
            if(mixer.getDescription() == description && mixer.getName() == name && mixer.getVendor() == vendor && mixer.getVersion() == version)
                return mixer;
        }
        
        return mixers.get(0);
    }
    
    public Mixer.Info getOutputMixer() {
        Element output = (Element) root.getElementsByTagName("outputMixer").item(0);
        String name = getTagValue("name", output);
        String description = getTagValue("description", output);
        String vendor = getTagValue("vendor", output);
        String version = getTagValue("version", output);
        ArrayList<Mixer.Info> mixers = new ArrayList<Mixer.Info>(AudioControl.getOutputDevices());
        
        for(Mixer.Info mixer: mixers) {
            if(mixer.getDescription().equals(description) && mixer.getName().equals(name) && mixer.getVendor().equals(vendor) && mixer.getVersion().equals(version)) {
                return mixer; 
            }
        }
        
        return mixers.get(0);
    }

    private static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

        Node nValue = nlList.item(0);

        return nValue.getNodeValue();
    }

}