package edu.cs.unc.sportsync.main.sound;

import java.util.ArrayList;
import java.util.Collection;

import javax.sound.sampled.*;

public class AudioControl {

	public static Collection<Mixer.Info> getInputDevices() {
		ArrayList<Mixer.Info> sourceMixers = new ArrayList<Mixer.Info>();
		Mixer.Info[] mixersInfo = AudioSystem.getMixerInfo();
		
		for(Mixer.Info info : mixersInfo) {
			Mixer mixer = AudioSystem.getMixer(info);
			
			Line.Info[] sourceLines = mixer.getSourceLineInfo();
			
			if (sourceLines.length > 0)
				sourceMixers.add(info);
		}
		
		return sourceMixers;
	}
	
	public static Collection<Mixer.Info> getOutputDevices() {
		ArrayList<Mixer.Info> targetMixers = new ArrayList<Mixer.Info>();
		Mixer.Info[] mixersInfo = AudioSystem.getMixerInfo();
		
		for(Mixer.Info info : mixersInfo) {
			Mixer mixer = AudioSystem.getMixer(info);
			
			Line.Info[] targetLines = mixer.getTargetLineInfo(); 
			
			if (targetLines.length > 0)
				targetMixers.add(info);
		}
		
		return targetMixers;
	}
}
