package com.peterfranza.gwt.svnadmin.server.entitydata.ldap;

import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class LDAPUserModule extends AbstractHibernateBeanModule{

	@Override
	protected void configureBeans() {
		bind(UserManager.class).to(LDAPUserManager.class);		
	}

}
