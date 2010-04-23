package com.peterfranza.gwt.svnadmin.server.datastore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule.HibernateBeanHolder;

@Singleton
public class HibernateSessionFactory implements Provider<Session> {

	private final PersistanceSubConfiguration configurator;
	private SessionFactory sessionFactory;
	private Injector injector;

	@Inject
	public HibernateSessionFactory(
			PersistanceSubConfiguration configurator,
			Injector injector) {	
		this.configurator = configurator;
		this.injector = injector;		
	}

	private synchronized SessionFactory getFactory() {	
		if(sessionFactory == null) {
			AnnotationConfiguration config = new AnnotationConfiguration();
			for(Class<?> cls: getAllHibernateClasses()) {
				config.addAnnotatedClass(cls);
				System.out.println("Binding Bean: " + cls);
			}

			configurator.configure(config);
			sessionFactory = config.buildSessionFactory();
		}
		return sessionFactory;
	}

	@SuppressWarnings("unchecked")
	private Collection<Class<?>> getAllHibernateClasses() {
		Collection<Class<?>> c = new ArrayList<Class<?>>();
		 List<Binding<HibernateBeanHolder>> l = injector.findBindingsByType( TypeLiteral
	                .get(HibernateBeanHolder.class ));
		 for(Binding<HibernateBeanHolder> b: l) {
			c.add(b.getProvider().get().getHibernateBeanClass()); 
		 }
		 return c;
	}

	@Override
	public Session get() {
		return getFactory().getCurrentSession();
	}
	
}
