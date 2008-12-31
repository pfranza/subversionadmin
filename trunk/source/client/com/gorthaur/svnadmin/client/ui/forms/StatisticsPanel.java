package com.gorthaur.svnadmin.client.ui.forms;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Image;
import com.gorthaur.svnadmin.client.SvnAdministration;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;

public class StatisticsPanel extends Panel {

	public StatisticsPanel() {
		final FormPanel form = new FormPanel();
		
		final FieldSet set = new FieldSet();
		set.setTitle("Disk Usage");
		set.setCollapsible(true);  
		set.setAutoHeight(true);

		addListener(new PanelListenerAdapter() {

			public void onShow(Component component) {
					set.clear();
					set.add(new Image("/images/diskUsage?username=" + 
							URL.encode(SvnAdministration.getInstance().getCredentials().getUsername())));
					form.doLayout();
					
				super.onShow(component);
			}
		});
		
		form.setTitle("Server Stats");  
		form.setFrame(false);  
		form.setPaddings(5, 5, 5, 0);  
//		form.setWidth(640);  
		form.setLabelWidth(75);  
						
		form.add(set);

		add(form);


	}
	
}
