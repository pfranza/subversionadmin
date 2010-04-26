package com.peterfranza.gwt.svnadmin.server.datastore;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class HibernateSessionFactory implements Provider<Session> {

	private final PersistanceSubConfiguration configurator;
	private SessionFactory sessionFactory;
	private HibernateBeanRegistry reg;

	@Inject
	public HibernateSessionFactory(
			PersistanceSubConfiguration configurator,
			HibernateBeanRegistry reg) {	
		this.configurator = configurator;
		this.reg = reg;		
	}

	private synchronized SessionFactory getFactory() {	
		if(sessionFactory == null) {
			AnnotationConfiguration config = new AnnotationConfiguration();
			for(Class<?> cls: reg.getBeans()) {
				config.addAnnotatedClass(cls);
				System.out.println("Binding Bean: " + cls);
			}

			configurator.configure(config);
			sessionFactory = config.buildSessionFactory();
		}
		return sessionFactory;
	}

	@Override
	public Session get() {
		return getFactory().openSession();
	}
	
}
