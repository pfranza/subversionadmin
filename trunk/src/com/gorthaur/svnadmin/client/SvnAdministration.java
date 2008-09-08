package com.gorthaur.svnadmin.client;

import com.google.gwt.core.client.EntryPoint;
import com.gorthaur.svnadmin.client.ui.LoginPanel;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SvnAdministration extends Panel implements EntryPoint {

	public SvnAdministration() {
		setBorder(false);  
		setPaddings(15);  
		setLayout(new FitLayout());
		
		final Window window = new Window();
			LoginPanel userdata = new LoginPanel(); 
			window.setTitle("Login");  
			window.setClosable(false);
			window.setResizable(false);
			window.setWidth(220);  
			window.setHeight(130);  
			window.setPlain(true); 
			window.setCloseAction(Window.CLOSE);	
			window.setLayout(new BorderLayout());  
			window.add(userdata, new BorderLayoutData(RegionPosition.CENTER));
			
			Panel buttons = new Panel();
			buttons.setBorder(false);
			buttons.setPaddings(0, 145, 5, 15);
			Button loginButton = new Button("Login");
			buttons.add(loginButton);
			
			window.add(buttons, new BorderLayoutData(RegionPosition.SOUTH));
			window.show(this.getId());


//		add(new MainPanel());
	}

	public void onModuleLoad() {
		new Viewport(this);
	}
}
