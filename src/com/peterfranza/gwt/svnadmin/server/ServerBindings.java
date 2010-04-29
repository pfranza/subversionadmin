package com.peterfranza.gwt.svnadmin.server;

import javax.servlet.ServletContextEvent;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ServerBindings extends GuiceServletContextListener {
	
	private ServerInjectorFactory injectorFactory;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		injectorFactory = new ServerInjectorFactory(servletContextEvent.getServletContext());
		super.contextInitialized(servletContextEvent);
	}
	
	@Override
	protected Injector getInjector() {
		return injectorFactory.getInjector();
	}
}
