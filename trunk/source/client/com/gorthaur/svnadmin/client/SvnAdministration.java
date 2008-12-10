package com.gorthaur.svnadmin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.gorthaur.svnadmin.client.ui.LoginWindow;
import com.gorthaur.svnadmin.client.ui.MainPanel;
import com.gorthaur.svnadmin.client.ui.LoginWindow.LoginWindowListener;
import com.gorthaur.svnadmin.client.ui.MainPanel.MainPanelListener;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SvnAdministration extends Panel implements EntryPoint {

	private static SvnAdministration instance;
	
	private final LoginWindow login = new LoginWindow();
	
	private final MainPanel mainPanel = new MainPanel() {{
		setVisible(false);
	}};

	private Viewport view;
	
	
	
	public SvnAdministration() {
		setBorder(false);  
		setPaddings(15);  
		setLayout(new FitLayout());
				
		add(mainPanel);		
		
		login.addListener(new LoginWindowListener() {
			public void loginSuccess() {				
				mainPanel.setVisible(true);
				login.hide();
				
				Timer t = new Timer() {
					public void run() {
						view.doLayout();
					}
				};
				t.schedule(500);
				
			}
		});
		
		mainPanel.addListener(new MainPanelListener() {
			public void logout() {
				mainPanel.setVisible(false);
				login.show(SvnAdministration.this.getId());
			}
		});
		
		login.show(this.getId());
		
	}

	public void onModuleLoad() {
		instance = this;
		view = new Viewport(this);	
	}
	
	public static SvnAdministration getInstance() {
		return instance;
	}
	
	public String getUsername() {
		return login.getUsername();
	}
	
	public String getPassword() {
		return login.getPassword();
	}
}
