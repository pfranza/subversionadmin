package com.peterfranza.gwt.svnadmin.server.mailer;

import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;

public class MailerModule extends ServletModule {
	
	private String smtpServer;
	private String smtpFromAddress;

	public MailerModule(String smtpServer, 
			String smtpFromAddress) {
		this.smtpServer = smtpServer;
		this.smtpFromAddress = smtpFromAddress;
	}
	
	@Override
	protected void configureServlets() {
		super.configureServlets();
		serve("/trigger/mail").with(MailTriggerServlet.class);
		bind(String.class).annotatedWith(Names.named("smtpFromAddress")).toInstance(smtpFromAddress);
		bind(String.class).annotatedWith(Names.named("smtpServer")).toInstance(smtpServer);
			
	}
	
}
