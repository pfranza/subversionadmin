package com.peterfranza.gwt.svnadmin.server.datastore;

import org.hibernate.Session;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SessionFactory implements Provider<Session> {

	private HibernateSessionFactory hsf;

	@Inject
	public SessionFactory(HibernateSessionFactory hsf) {
		this.hsf = hsf;
	}

	@Override
	public Session get() {
		return hsf.getSession();
	}
	
}
