package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class LocalUserModule extends AbstractHibernateBeanModule{

	@Override
	protected void configureBeans() {
		bindBean(HbmUserImpl.class);
		bind(UserManager.class).to(LocalUserManager.class);		
	}

}
