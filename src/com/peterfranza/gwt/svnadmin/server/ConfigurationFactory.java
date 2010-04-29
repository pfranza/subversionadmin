package com.peterfranza.gwt.svnadmin.server;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

import com.google.inject.Provider;

public class ConfigurationFactory implements Provider<Configuration>{

	private static Configuration configuration;
	
	static {
		CompositeConfiguration cc = new CompositeConfiguration();
		cc.addConfiguration(new SystemConfiguration());
		cc.addConfiguration(new EnvironmentConfiguration());		
		configuration = cc;
	}
	
	public static Configuration getConfig()  {
		return configuration;
	}
	
	@Override
	public Configuration get() {
		return getConfig();
	}

}
