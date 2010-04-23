package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.entitydata.Group;

@javax.persistence.Entity
public class HbmGroupImpl implements Serializable, Group {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1898458363908642997L;

	@GeneratedValue
	@Id
	private long id;
	
	private String name;
	private Collection<? extends Entity> members;
	
	@Override
	public Collection<? extends Entity> getMembers() {
		return members;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public long getId() {
		return id;
	}

}
