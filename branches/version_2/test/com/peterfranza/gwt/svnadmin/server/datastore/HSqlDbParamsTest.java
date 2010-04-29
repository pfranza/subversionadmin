package com.peterfranza.gwt.svnadmin.server.datastore;

import junit.framework.TestCase;

import org.hibernate.cfg.AnnotationConfiguration;

public class HSqlDbParamsTest extends TestCase {

	public void testInit() throws Exception {
		HSqlDbParams params = new HSqlDbParams("test", "");
		params.configure(new AnnotationConfiguration());
	}
	
}
