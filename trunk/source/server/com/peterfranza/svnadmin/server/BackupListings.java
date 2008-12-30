package com.peterfranza.svnadmin.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class BackupListings {

	public static List<String> getFileNames() {
		File f = new File(ApplicationProperties.getProperty("backups_directory"));
		if(f.isDirectory()) {
			ArrayList<String> l = new ArrayList<String>();
			for(File c: f.listFiles()) {
				if(c.canRead()) {
					l.add(c.getName());
				}
			}
			return l;
		} else {
			throw new RuntimeException("'" + ApplicationProperties.getProperty("backups_directory") + "' is not a directory. ");
		}
	}
	
	public static File getFile(String fileName) {
		return new File(ApplicationProperties.getProperty("backups_directory"), fileName);
	}
	
}
