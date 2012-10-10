package edu.unc.cs.sportsync.main.ui.application;

import org.eclipse.swt.widgets.ProgressBar;

public class ProgressBarUpdater implements Runnable {

    private final ProgressBar bufferProgressBar;
    private final int percent;
    private final boolean isRecording;

    public ProgressBarUpdater(ProgressBar bar, int p, boolean recording) {
        bufferProgressBar = bar;
        percent = p;
        isRecording = recording;
    }

    @Override
    public void run() {
        if (!isRecording || percent == 100) {
            bufferProgressBar.setVisible(false);
        } else {
            bufferProgressBar.setVisible(true);
            bufferProgressBar.setSelection(percent);
        }
    }

}
