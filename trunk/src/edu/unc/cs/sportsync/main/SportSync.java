package edu.unc.cs.sportsync.main;

import edu.unc.cs.sportsync.main.ui.application.ApplicationPresenter;

public class SportSync {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ApplicationPresenter app = new ApplicationPresenter();
        app.open();
        app.dispose();
    }

}