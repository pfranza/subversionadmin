package com.peterfranza.gwt.svnadmin.server.repositorydata.svn;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.peterfranza.gwt.svnadmin.server.repositorydata.Project;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager.ACCESS;

@Entity
public class SvnProjectBean implements Serializable, Project{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7614248181767208529L;

	@GeneratedValue
	@Id
	private long id;

	private ArrayList<String> subscribers = new ArrayList<String>();
	private ArrayList<String> writers = new ArrayList<String>();
	private ArrayList<String> readers = new ArrayList<String>();
	
	private String path;
	
	public SvnProjectBean() {
		
	}
	
	public SvnProjectBean(String path) {
		this.path = path;
	}

	@Override
	public String getPath() {
		return path;
	}

	public boolean canRead(String entity) {
		return canWrite(entity) || readers.contains(entity);
	}

	public boolean canWrite(String entity) {
		return writers.contains(entity);
	}

	public boolean isSubscribed(String entity) {
		return subscribers.contains(entity);
	}

	public void subscribe(String entity) {
		if(!isSubscribed(entity)) {
			subscribers.add(entity);
		}
	}

	public void unsubscribe(String entity) {
		subscribers.remove(entity);
	}

	public void setAccess(String entity, ACCESS access) {
		writers.remove(entity);
		readers.remove(entity);
		
		switch (access) {
		case WRITE:
			writers.add(entity);
			break;
			
		case READ: 
			readers.add(entity);
			break;
			
		case NONE:
			break;

		}
		
	}

	
}
