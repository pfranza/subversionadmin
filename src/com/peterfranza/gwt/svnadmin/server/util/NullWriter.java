package com.peterfranza.gwt.svnadmin.server.util;

public class NullWriter extends ConfigFileWriter{

	public NullWriter() {
		super(null);
	}
	
	@Override
	public void save(String str) {
		
	}
	
}
