package com.gorthaur.svnadmin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.gorthaur.svnadmin.client.ui.LoginWindow;
import com.gorthaur.svnadmin.client.ui.MainPanel;
import com.gorthaur.svnadmin.client.ui.LoginWindow.LoginWindowListener;
import com.gorthaur.svnadmin.client.ui.MainPanel.MainPanelListener;
import com.gwtext.client.widgets.Viewport;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SvnAdministration  implements EntryPoint {

	private static SvnAdministration instance;
	
	private final LoginWindow login = new LoginWindow();
	
	private final MainPanel mainPanel = new MainPanel() {{
		setMonitorResize(true);
		setMaskDisabled(true);
	}};

	
	
	public SvnAdministration() {
	
		
		login.addListener(new LoginWindowListener() {
			
			public void loginSuccess() {		
				hideLogin();
			}
		});
		
		mainPanel.addListener(new MainPanelListener() {
			public void logout() {
				displayLogin();
			}
		});
		
	}

	private void displayLogin() {
		login.show();
		login.center();
		mainPanel.setDisabled(true);

	}
	
	private void hideLogin() {
		login.hide();
		mainPanel.setDisabled(false);
	}

	public void onModuleLoad() {
		instance = this;
		
		Timer t = new Timer() {
			public void run() {
				initDisplay();
			}
		};
		t.schedule(500);
		
		Window.setTitle("SVN Administration");
		
	}

	private void initDisplay() {
		new Viewport(mainPanel);
		
		displayLogin();
		Window.addWindowResizeListener(new WindowResizeListener() {

			public void onWindowResized(int width, int height) {
				mainPanel.setSize(width, height);
				if(login.isVisible()) {
					login.center();
				}
			}
			
		});
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
