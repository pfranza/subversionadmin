package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import com.peterfranza.gwt.svnadmin.server.datastore.AbstractHibernateBeanModule;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;

public class LocalGroupModule extends AbstractHibernateBeanModule{

	@Override
	protected void configureBeans() {
		bindBean(HbmGroupImpl.class);
		bind(GroupManager.class).to(LocalGroupManager.class);
	}

}
