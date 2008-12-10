package com.gorthaur.svnadmin.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.gorthaur.svnadmin.client.ui.listeners.ClickListener;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.layout.FitLayout;

public class MainPanel extends Panel {

	private List<MainPanelListener> listeners = new ArrayList<MainPanelListener>();
	private ActionPanel actionPanel = new ActionPanel();
	
	public MainPanel() {

		setLayout(new FitLayout());
		setPaddings(0);
		setBorder(true);
		setTitle("SVN Administration");
		
		createToolbar();
		add(actionPanel);

	}

	private void createToolbar() {
		Toolbar toolbar = new Toolbar();
			toolbar.addFill();
		
			toolbar.addButton(new ToolbarButton("Logout", new ClickListener() {
				public void onClick(Button button, EventObject e) {
					for(MainPanelListener l: listeners) {
						l.logout();
					}
				}
			}));
			
		setTopToolbar(toolbar);
	}
	
	public void addListener(MainPanelListener listener) {
		listeners.add(listener);
	}
	
	public interface MainPanelListener {
		void logout();
	}
	
}
