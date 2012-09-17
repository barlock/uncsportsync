package edu.cs.unc.sportsync.main.sound;

import java.util.ArrayList;
import java.util.Collection;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.EnumControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class AudioControl {
    public static Collection<Mixer.Info> getInputDevices() {
	ArrayList<Mixer.Info> sourceMixers = new ArrayList<Mixer.Info>();

	for (Mixer.Info thisMixerInfo : AudioSystem.getMixerInfo()) {
	    // System.out.println("Mixer: "+thisMixerInfo.getDescription() +
	    // " ["+thisMixerInfo.getName()+"]");
	    Mixer thisMixer = AudioSystem.getMixer(thisMixerInfo);

	    for (Line.Info thisLineInfo : thisMixer.getSourceLineInfo()) {
		if (thisLineInfo.getLineClass().getName().equals("javax.sound.sampled.Port")) {
		    Line thisLine;

		    try {
			thisLine = thisMixer.getLine(thisLineInfo);
			thisLine.open();

			sourceMixers.add(thisMixerInfo);
			// System.out.println("  Source Port: " +
			// thisLineInfo.toString());

			for (Control thisControl : thisLine.getControls()) {
			    // System.out.println(AnalyzeControl(thisControl));
			}

			thisLine.close();
		    } catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
	    }
	}

	return sourceMixers;
    }

    public static Collection<Mixer.Info> getOutputDevices() {
	ArrayList<Mixer.Info> sourceMixers = new ArrayList<Mixer.Info>();

	for (Mixer.Info thisMixerInfo : AudioSystem.getMixerInfo()) {
	    // System.out.println("Mixer: " + thisMixerInfo.getDescription() +
	    // " [" + thisMixerInfo.getName() + "]");
	    Mixer thisMixer = AudioSystem.getMixer(thisMixerInfo);

	    for (Line.Info thisLineInfo : thisMixer.getTargetLineInfo()) {
		if (thisLineInfo.getLineClass().getName().equals("javax.sound.sampled.Port")) {
		    Line thisLine;

		    try {
			thisLine = thisMixer.getLine(thisLineInfo);
			thisLine.open();

			sourceMixers.add(thisMixerInfo);
			// System.out.println("  Source Port: " +
			// thisLineInfo.toString());

			for (Control thisControl : thisLine.getControls()) {
			    // System.out.println(AnalyzeControl(thisControl));
			}

			thisLine.close();
		    } catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
	    }
	}

	return sourceMixers;
    }

    public static String AnalyzeControl(Control thisControl) {
	String type = thisControl.getType().toString();

	if (thisControl instanceof BooleanControl) {
	    return "\tControl: " + type + " (boolean)";
	}

	if (thisControl instanceof CompoundControl) {
	    System.out.println("\tControl: " + type + " (compound - values below)");
	    String toReturn = "";
	    for (Control children : ((CompoundControl) thisControl).getMemberControls()) {
		toReturn += "  " + AnalyzeControl(children) + "\n";
	    }
	    return toReturn.substring(0, toReturn.length() - 1);
	}

	if (thisControl instanceof EnumControl) {
	    return "\tControl:" + type + " (enum: " + thisControl.toString() + ")";
	}

	if (thisControl instanceof FloatControl) {
	    return "\tControl: " + type + " (float: from " + ((FloatControl) thisControl).getMinimum() + " to " + ((FloatControl) thisControl).getMaximum() + ")";
	}
	return "\tControl: unknown type";
    }
}
