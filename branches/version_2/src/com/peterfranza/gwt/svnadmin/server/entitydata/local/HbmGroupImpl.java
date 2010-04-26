package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import java.io.Serializable;
import java.util.ArrayList;
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

	@SuppressWarnings("unused")
	@GeneratedValue
	@Id
	private long id;
	
	private String name;
	
	private ArrayList<String> members = new ArrayList<String>();
	
	public Collection<String> getStringMembers() {
		return members;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String groupName) {
		this.name = groupName;
	}

	public void removeMember(Entity entity) {
		if(entity instanceof Group) {
			members.remove("@" + entity.getName());
		} else {
			members.remove(entity.getName());
		}
	}

	public void addMember(Entity entity) {
		if(entity instanceof Group) {
			members.add("@" + entity.getName());
		} else {
			members.add(entity.getName());
		}
	}

	@Override
	public Collection<Entity> getMembers() {
		throw new RuntimeException("Fail Needs Override");
	}

}
