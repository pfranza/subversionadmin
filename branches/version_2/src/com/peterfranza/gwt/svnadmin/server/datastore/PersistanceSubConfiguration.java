/**
 * 
 */
package com.peterfranza.gwt.svnadmin.server.datastore;

import org.hibernate.cfg.AnnotationConfiguration;

public interface PersistanceSubConfiguration {
	void configure(AnnotationConfiguration config);
}