package com.peterfranza.gwt.svnadmin.client.actions;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MessageResultHandler implements AsyncCallback<MessageResult>{

	@Override
	public void onFailure(Throwable caught) {
		caught.printStackTrace();
	}

	@Override
	public void onSuccess(MessageResult result) {
		if(result.getTitle() != null && result.getTitle().length() > 0)  {
			final Dialog simple = new Dialog();  
			simple.setHeading(result.getTitle());  
			simple.setButtons(Dialog.OK);  
			simple.setBodyStyleName("pad-text");  
			simple.addText(result.getMessage());  
			simple.setScrollMode(Scroll.AUTO);  
			simple.setHideOnButtonClick(true);
		} else {
			Info.display("MessageBox", "{0}", new Params(result.getMessage()));
		}
	}

}
