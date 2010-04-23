package com.peterfranza.gwt.svnadmin.server.configurationexport;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultConfigurationExportManager.class)
public interface ConfigurationExportManager {
	void exportPasswordFile();
	void exportPermissionsFile();
}
