package com.peterfranza.gwt.svnadmin.client;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.gin.ClientDispatchModule;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.PlaceManager;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ClientDispatchModule.class, BindingModule.class})
public interface SInjector extends Ginjector {

	PlaceManager getPlaceManager();
	EventBus getEventBus();
	DispatchAsync getDispatcher();
	
}
