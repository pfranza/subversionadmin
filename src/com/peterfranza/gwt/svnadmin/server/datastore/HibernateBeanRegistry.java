package com.peterfranza.gwt.svnadmin.server.datastore;

import java.util.Collection;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultHibernateBeanRegistry.class)
public interface HibernateBeanRegistry {

	Collection<Class<?>> getBeans();
	
}
