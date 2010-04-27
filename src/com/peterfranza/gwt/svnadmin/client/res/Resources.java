package com.peterfranza.gwt.svnadmin.client.res;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	public static final Resources INSTANCE =  GWT.create(Resources.class);

	@Source("cog.png")
	ImageResource cog();
	
	@Source("cross.png")
	ImageResource cross();

}
