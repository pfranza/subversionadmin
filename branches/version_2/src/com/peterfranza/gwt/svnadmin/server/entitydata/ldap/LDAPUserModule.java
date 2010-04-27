package com.peterfranza.gwt.svnadmin.server.entitydata.ldap;

import com.google.inject.name.Names;
import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class LDAPUserModule extends AbstractHibernateBeanModule{

	private String ldapUrl;
	private String ldapUsername;
	private String ldapPassword;
	private String ldapAdminGroup;

	public LDAPUserModule(String ldapUrl,
		String ldapUsername,
		String ldapPassword,
		String ldapAdminGroup) {
		this.ldapUrl = ldapUrl;
		this.ldapUsername = ldapUsername;
		this.ldapPassword = ldapPassword;
		this.ldapAdminGroup = ldapAdminGroup;
	}
	
	@Override
	protected void configureBeans() {
		bind(String.class).annotatedWith(Names.named("ldapUrl")).toInstance(ldapUrl);
		bind(String.class).annotatedWith(Names.named("ldapUsername")).toInstance(ldapUsername);
		bind(String.class).annotatedWith(Names.named("ldapPassword")).toInstance(ldapPassword);
		bind(String.class).annotatedWith(Names.named("ldapAdminGroup")).toInstance(ldapAdminGroup);		
		bind(UserManager.class).to(LDAPUserManager.class);		
	}

}
