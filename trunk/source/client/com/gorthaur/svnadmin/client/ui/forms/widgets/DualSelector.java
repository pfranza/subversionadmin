package com.gorthaur.svnadmin.client.ui.forms.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class DualSelector extends HorizontalPanel {
	
	private ListBox member = new ListBox(true) {{
		setSize("200px", "200px");
	}};
	
	private ListBox available = new ListBox(true) {{
		setSize("200px", "200px");
	}};

	private Button left = new Button("<<") {

		{
			addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					int i = -1;
					boolean flag = false;
					while( (i = available.getSelectedIndex()) != -1) {
						member.addItem(available.getItemText(i));
						available.removeItem(i);
						flag = true;
					}
					if(flag) {
						sort(member);
					}
				}
			});
		}
	};
	
	
	private Button right = new Button(">>"){{
		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int i = -1;
				boolean flag = false;
				while( (i = member.getSelectedIndex()) != -1) {
					available.addItem(member.getItemText(i));
					member.removeItem(i);
					flag = true;
				}
				if(flag) {
					sort(available);
				}
			}
			
		});		
	}};
	
	private void sort(ListBox l) {
		List<String> a = new ArrayList<String>();
		for(int i = 0; i < l.getItemCount(); i++) {
			a.add(l.getItemText(i));
		}
		Collections.sort(a);
		l.clear();
		for(String s: a) {
			l.addItem(s);
		}
	}

	private String[] origionalIn;
	private String[] origionalOut;
	
	public DualSelector() {
		
		setSpacing(10);
		
		add(member);
		
		VerticalPanel vp = new VerticalPanel();
			vp.setSpacing(5);
			vp.add(left);
			vp.add(right);
			
		add(vp);
		setCellVerticalAlignment(vp, HorizontalPanel.ALIGN_MIDDLE);
		
		add(available);
		
	}
	
	public void populate(String[] in, String[] out) {
		populateIncluded(in);
		populateExcluded(out);
	}
	
	public List<String> getItemsToAdd() {
		List<String> l = new ArrayList<String>();
		for(int i = 0; i < member.getItemCount(); i++) {
			if(!isInOrigional(origionalIn, member.getItemText(i))) {
				l.add(member.getItemText(i));
			}
		}
		return l;
	}
	


	public List<String> getItemsToRemove() {
		List<String> l = new ArrayList<String>();
		for(int i = 0; i < available.getItemCount(); i++) {
			if(!isInOrigional(origionalOut, available.getItemText(i))) {
				l.add(available.getItemText(i));
			}
		}
		return l;
	}
	
	
	private boolean isInOrigional(String[] orig, String itemText) {
		if(orig != null) {
			for(String s: orig) {
				if(itemText.equals(s)) {
					return true;
				}
			}
		}
		return false;
	}

	public void reset() {
		member.clear();
		available.clear();
	}

	public void populateIncluded(String[] in) {
		this.origionalIn = in;
		member.clear();
		try {
			for(String s: in) {
				member.addItem(s.trim());
			}
		} catch (Exception e) {}
		sort(member);
	}
	
	public void populateExcluded(String[] out) {
		this.origionalOut = out;
		available.clear();

		try {
			for(String s: out) {
				available.addItem(s.trim());
			}
		} catch (Exception e) {}
		
		sort(available);
	}
	
	
}
