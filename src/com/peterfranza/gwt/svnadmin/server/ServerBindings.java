package com.peterfranza.gwt.svnadmin.server;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ServerBindings extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		return new ServerInjectorFactory().getInjector();
	}
}
