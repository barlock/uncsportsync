package edu.unc.cs.sportsync.main.sound;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.EnumControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioControl {
    private static SoundCheck mySoundCheck;

    @SuppressWarnings("unused")
    private static String AnalyzeControl(Control thisControl) {
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

    public static ArrayList<Mixer.Info> getInputDevices() {
        ArrayList<Mixer.Info> targetMixers = new ArrayList<Mixer.Info>();

        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        for (int i = 0; i < mixerInfos.length; i++) {
            Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);
            try {
                mixer.open();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            Line.Info[] targetLines = mixer.getTargetLineInfo();
            for (Line.Info info : targetLines) {
                if (info.getLineClass() == TargetDataLine.class) {
                    targetMixers.add(mixerInfos[i]);
                }
            }
            mixer.close();
        }

        return targetMixers;
    }

    public static ArrayList<Mixer.Info> getOutputDevices() {
        ArrayList<Mixer.Info> sourceMixers = new ArrayList<Mixer.Info>();

        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        for (int i = 0; i < mixerInfos.length; i++) {
            Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);
            try {
                mixer.open();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            Line.Info[] sourceLines = mixer.getSourceLineInfo();
            for (Line.Info info : sourceLines) {
                if (info.getLineClass() == SourceDataLine.class) {
                    sourceMixers.add(mixerInfos[i]);
                }
            }
            mixer.close();
        }

        return sourceMixers;
    }

    public static void setVolume(double percentLevel) {
        mySoundCheck.setVolume(percentLevel);
    }

    public static void start() {
        float frameRate = (float) 44100.0;
        int BUFFER_SIZE = 40960;

        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, frameRate, 16, 2, 4, frameRate, false);
        mySoundCheck = null;
        try {
            mySoundCheck = new SoundCheck(audioFormat, BUFFER_SIZE);
        } catch (LineUnavailableException e) {
            System.exit(1);
        }
        mySoundCheck.start();
    }

    public static void stopRecording() {
        mySoundCheck.stopRecording();
    }

    public static void toggleMute() {
        mySoundCheck.toggleMute();
    }
}
