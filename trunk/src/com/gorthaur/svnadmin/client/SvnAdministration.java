package com.gorthaur.svnadmin.client;

import com.google.gwt.core.client.EntryPoint;
import com.gorthaur.svnadmin.client.ui.LoginWindow;
import com.gorthaur.svnadmin.client.ui.MainPanel;
import com.gorthaur.svnadmin.client.ui.LoginWindow.LoginWindowListener;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SvnAdministration extends Panel implements EntryPoint {

	public SvnAdministration() {
		setBorder(false);  
		setPaddings(15);  
		setLayout(new FitLayout());
		
		final LoginWindow login = new LoginWindow();
		login.addListener(new LoginWindowListener() {

			public void loginSuccess() {				
				add(new MainPanel());
				SvnAdministration.this.doLayout();
				login.hide();
			}
			
		});
		login.show(this.getId());
		
	}

	public void onModuleLoad() {
		new Viewport(this);
	}
}
