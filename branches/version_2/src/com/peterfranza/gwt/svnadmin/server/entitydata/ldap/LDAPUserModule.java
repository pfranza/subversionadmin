package com.peterfranza.gwt.svnadmin.server.entitydata.ldap;

import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class LDAPUserModule extends AbstractHibernateBeanModule{

	private String ldapUrl;
	private String ldapUsername;
	private String ldapPassword;
	private String ldapAdminGroup;
	private String ldapQuery;
	private String[] ldapQueryAttrs;
	private String usernameKey;
	private String mailKey;
	private String principleKey;
	private String memberOfKey;

	public LDAPUserModule(String ldapUrl,
		String ldapUsername,
		String ldapPassword,
		String ldapAdminGroup,
		String ldapQuery,
		String[] ldapQueryAttrs,
		String usernameKey,
		String mailKey,
		String principleKey,
		String memberOfKey) {
		this.ldapUrl = ldapUrl;
		this.ldapUsername = ldapUsername;
		this.ldapPassword = ldapPassword;
		this.ldapAdminGroup = ldapAdminGroup;
		this.ldapQuery = ldapQuery;
		this.ldapQueryAttrs = ldapQueryAttrs;
		this.usernameKey = usernameKey;
		this.mailKey = mailKey;
		this.principleKey = principleKey;
		this.memberOfKey = memberOfKey;
	}
	
	@Override
	protected void configureBeans() {
		
		Provider<String[]> attrsProvider = new Provider<String[]>(){
			@Override
			public String[] get() {
				return ldapQueryAttrs;
			}
		};

		bind(String.class).annotatedWith(Names.named("ldapUrl")).toInstance(ldapUrl);
		bind(String.class).annotatedWith(Names.named("ldapUsername")).toInstance(ldapUsername);
		bind(String.class).annotatedWith(Names.named("ldapPassword")).toInstance(ldapPassword);
		bind(String.class).annotatedWith(Names.named("ldapAdminGroup")).toInstance(ldapAdminGroup);
		bind(String.class).annotatedWith(Names.named("ldapQuery")).toInstance(ldapQuery);
		bind(String[].class).annotatedWith(Names.named("ldapQueryAttributes")).toProvider(attrsProvider);
		bind(String.class).annotatedWith(Names.named("usernameKey")).toInstance(usernameKey);
		bind(String.class).annotatedWith(Names.named("mailKey")).toInstance(mailKey);
		bind(String.class).annotatedWith(Names.named("principleKey")).toInstance(principleKey);
		bind(String.class).annotatedWith(Names.named("memberOfKey")).toInstance(memberOfKey);
		bind(UserManager.class).to(LDAPUserManager.class);		
	}

}
