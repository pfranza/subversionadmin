package com.peterfranza.gwt.svnadmin.server.util;

import java.io.File;
import java.io.FileWriter;

public class ConfigFileWriter {

	private File f;

	public ConfigFileWriter(File f) {
		this.f = f;
	}
	
	public void save(String str) {
		try {
			FileWriter w = new FileWriter(f);
			w.write(str);
			w.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
