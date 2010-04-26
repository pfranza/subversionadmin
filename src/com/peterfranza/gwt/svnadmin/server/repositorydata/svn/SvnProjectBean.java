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

	private String path;
	
	public SvnProjectBean() {
		
	}
	
	public SvnProjectBean(String path) {
		this.path = path;
	}
	
	public long getId() {
		return id;
	}

	@Override
	public String getPath() {
		return path;
	}

	public boolean canRead(String entity) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canWrite(String entity) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSubscribed(String entity) {
		// TODO Auto-generated method stub
		return false;
	}

	public void subscribe(String entity) {
		// TODO Auto-generated method stub
		
	}

	public void unsubscribe(String entity) {
		// TODO Auto-generated method stub
		
	}

	
}
