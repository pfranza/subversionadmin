package com.gorthaur.svnadmin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;
import com.gorthaur.svnadmin.client.ui.LoginWindow;
import com.gorthaur.svnadmin.client.ui.MainPanel;
import com.gwtext.client.widgets.Viewport;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SvnAdministration  implements EntryPoint {

	private static SvnAdministration instance;
	
	private final LoginWindow login = new LoginWindow(){{
		
		addListener(new LoginWindowListener() {
			public void loginSuccess() {		
				hideLogin();
			}
		});
		
	}};
	
	private final MainPanel mainPanel = new MainPanel() {{
		setMonitorResize(true);
		setMaskDisabled(true);
		
		addListener(new MainPanelListener() {
			public void logout() {
				displayLogin();
			}
		});
		
	}};


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
		
		RootPanel.get().add(mainPanel);
				
		Window.setTitle("SVN Administration");
		
	}

	private void initDisplay() {
		mainPanel.removeFromParent();
		new Viewport(mainPanel);
		displayLogin();
		Window.addResizeHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				mainPanel.setSize(event.getWidth(), event.getHeight());
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

	public Credentials getCredentials() {
		return new Credentials(getUsername(), getPassword());
	}
}
