package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.peterfranza.gwt.svnadmin.server.repositorydata.Project;

@Entity
public class SvnProjectBean implements Serializable, Project{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7614248181767208529L;

	@GeneratedValue
	@Id
	private long id;

	private String name;
	private String path;
	
	public SvnProjectBean() {
		
	}
	
	public SvnProjectBean(String path) {
		this.name = path;
		this.path = path;
	}
	
	@Override
	public boolean canRead(
			com.peterfranza.gwt.svnadmin.server.entitydata.Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canWrite(
			com.peterfranza.gwt.svnadmin.server.entitydata.Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public long getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public boolean isSubscribed(
			com.peterfranza.gwt.svnadmin.server.entitydata.Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void subscribe(
			com.peterfranza.gwt.svnadmin.server.entitydata.Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(
			com.peterfranza.gwt.svnadmin.server.entitydata.Entity entity) {
		// TODO Auto-generated method stub
		
	}

	
}
