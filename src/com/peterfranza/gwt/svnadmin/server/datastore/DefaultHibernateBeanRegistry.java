package com.peterfranza.gwt.svnadmin.server.datastore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule.HibernateBeanHolder;

public class DefaultHibernateBeanRegistry implements HibernateBeanRegistry {

	private Injector injector;

	@Inject
	public DefaultHibernateBeanRegistry(Injector injector) {
		this.injector = injector;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Class<?>> getBeans() {
		Collection<Class<?>> c = new ArrayList<Class<?>>();
		 List<Binding<HibernateBeanHolder>> l = injector.findBindingsByType( TypeLiteral
	                .get(HibernateBeanHolder.class ));
		 for(Binding<HibernateBeanHolder> b: l) {
			c.add(b.getProvider().get().getHibernateBeanClass()); 
		 }
		 return c;
	}

}
