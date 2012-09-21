package edu.unc.cs.sportsync.main.ui.settings;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import edu.unc.cs.sportsync.main.ui.IView;

public abstract class ISettingsTabView extends TabFolder implements IView {

    public ISettingsTabView(Composite parent, int style) {
        super(parent, style);
        // TODO Auto-generated constructor stub
    }

}
