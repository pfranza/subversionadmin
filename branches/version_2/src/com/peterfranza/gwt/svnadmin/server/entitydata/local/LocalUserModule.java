package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import com.google.inject.name.Names;
import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.util.ConfigFileWriter;

public class LocalUserModule extends AbstractHibernateBeanModule{

	private ConfigFileWriter file;

	public LocalUserModule(ConfigFileWriter file) {
		this.file = file;
	}

	@Override
	protected void configureBeans() {
		bind(ConfigFileWriter.class).annotatedWith(Names.named("passwordFile")).toInstance(file);
		bindBean(HbmUserImpl.class);
		bind(UserManager.class).to(LocalUserManager.class);		
	}

}
