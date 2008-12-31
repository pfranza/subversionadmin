package com.gorthaur.svnadmin.client.ui.forms;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gorthaur.svnadmin.client.SvnAdministration;
import com.gorthaur.svnadmin.client.rpcinterface.AdminOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.AdminOperationsInterfaceAsync;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;

public class BackupsDownloadForm extends Panel{

	private VerticalPanel links = new VerticalPanel();
	
	public BackupsDownloadForm() {
	
		addListener(new PanelListenerAdapter() {
			@Override
			public void onShow(Component component) {
				AdminOperationsInterfaceAsync a = GWT.create(AdminOperationsInterface.class);
				a.getBackupFilenames(SvnAdministration.getInstance().getCredentials(), 
						new AsyncCallback<List<String>>() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(List<String> result) {
						renderLinks(result);
					}
					
				});
				
				super.onShow(component);
			}
		});
		
		setBorder(false);
		setWidth(500);
		setPaddings(15);
		
		FormPanel form = new FormPanel();
		
		form.setTitle("Backups");  
		form.setFrame(false);  
		form.setPaddings(5, 5, 5, 0);  
		form.setWidth(450);  
		form.setLabelWidth(75); 
		form.add(links);
		setAutoScroll(true);
		add(form);
		
	}
	
	private void renderLinks(List<String> result) {
		links.clear();
		Credentials cred = SvnAdministration.getInstance().getCredentials();
		for (Iterator<String> iterator = result.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			links.add(new HTML(
					"<a href=\"/backups/" + string + "?username=" 
					+ URL.encode(cred.getUsername()) + "\">" + string + "</a>" ));
		}
	}
	
	
}
