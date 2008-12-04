package com.peterfranza.svnadmin.server;

import java.io.FileInputStream;
import java.util.Properties;

public class ApplicationProperties {

	private static Properties props;
	
	static {
		props = new Properties();
		try {
			FileInputStream in = new FileInputStream(System.getProperty("ConfigFile", "config.properties"));
			props.load(in);
			in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String getProperty(String key) {
		return props.getProperty(key);
	}
	
}
