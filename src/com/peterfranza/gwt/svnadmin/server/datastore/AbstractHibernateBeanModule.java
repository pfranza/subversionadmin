package com.peterfranza.gwt.svnadmin.server.datastore;

import com.google.inject.AbstractModule;
import com.google.inject.internal.UniqueAnnotations;


public abstract class AbstractHibernateBeanModule extends AbstractModule {

	protected interface HibernateBeanHolder<T> {
		Class<T> getHibernateBeanClass();
	}

	private static class HibernateBeanHolderImpl<A> 
	implements HibernateBeanHolder<A> {

		private final Class<A> beanClass;

		public HibernateBeanHolderImpl(Class<A> actionClass) {
			this.beanClass = actionClass;
		}

		@Override
		public Class<A> getHibernateBeanClass() {
			return beanClass;
		}

	}

	@Override
	protected final void configure() {
		configureBeans();
	}

	/**
	 * Override this method to configure handlers.
	 */
	protected abstract void configureBeans();

	protected <A> void bindBean( Class<A> bean ) {
        bind( HibernateBeanHolder.class ).annotatedWith( UniqueAnnotations.create() ).toInstance(
                new HibernateBeanHolderImpl<A>( bean ) );
      
	}
	
}
