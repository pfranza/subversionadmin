package com.peterfranza.gwt.svnadmin.server;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.configuration.web.ServletContextConfiguration;

import com.google.inject.Provider;

public class ConfigurationFactory implements Provider<Configuration>{

	private Configuration configuration;
	
	public ConfigurationFactory(ServletContext servlet) {
		CompositeConfiguration cc = new CompositeConfiguration();
		cc.addConfiguration(new ServletContextConfiguration(servlet));
		cc.addConfiguration(new SystemConfiguration());
		cc.addConfiguration(new EnvironmentConfiguration());
		
		String pf = cc.getString("subversionadmin.configuration");
		File pfp = new File(pf);
		CompositeConfiguration cc2 = new CompositeConfiguration();
		if(pfp.exists()) {
			try {
				cc2.addConfiguration(new PropertiesConfiguration(pfp));
				System.out.println("Loaded configuration from: " + pfp.getAbsolutePath());
				servlet.log("Loaded configuration from: " + pfp.getAbsolutePath());
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
		cc2.addConfiguration(cc);
		
		configuration = cc2;
	}
	
	@Override
	public Configuration get() {
		return configuration;
	}

}
