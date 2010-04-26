package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.util.Crypt;

public class LocalUserManager implements UserManager{

	private Crypt crypt;
	private Provider<Session> sessionProvider;


	@Inject
	public LocalUserManager(Crypt crypt,
			Provider<Session> sessionProvider) {
		this.crypt = crypt;
		this.sessionProvider = sessionProvider;
	}
	
	@Override
	public HbmUserImpl getUserForName(String username) {
		for(HbmUserImpl u: getUsers()) {
			if(u.getName().equals(username)) {
				return u;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<HbmUserImpl> getUsers() {
		Session s = sessionProvider.get();
		Transaction tx = s.beginTransaction();
		try {
			return s.createCriteria(HbmUserImpl.class).list();
		} finally {
			tx.commit();
		}
	}
	
	@Override
	public boolean authenticate(String username, String password) {
		User u = getUserForName(username);
		return u != null ? crypt.matches(u.getPassword(), password) : false;
	}
	
	@Override
	public void removeUser(String username) {
		Session s = sessionProvider.get();
		Transaction tx = s.beginTransaction();
		try {
			User u = getUserForName(username);
			if(u != null) {
				s.delete(u);
			}
		} finally {
			tx.commit();
		}
	}
	
	@Override
	public void createUser(String username) {
		Session s = sessionProvider.get();
		Transaction tx = s.beginTransaction();
		try {
			HbmUserImpl u = getUserForName(username);
			if(u == null) {
				u = new HbmUserImpl();
				u.setName(username);
				s.saveOrUpdate(u);
			} else {
				throw new RuntimeException("User Exists");
			}
		} finally {
			tx.commit();
		}
	}
	
	@Override
	public void setPassword(String username, String password) {
		Session s = sessionProvider.get();
		Transaction tx = s.beginTransaction();
		try {
			HbmUserImpl u = getUserForName(username);
			if(u != null) {
				u.setPassword(crypt.crypt(password));
				s.update(u);
			} else {
				throw new RuntimeException("User not found");
			}
		} finally {
			tx.commit();
		}
	}
	
	@Override
	public void setEmailAddress(String username, String email) {
		Session s = sessionProvider.get();
		Transaction tx = s.beginTransaction();
		try {
			HbmUserImpl u = getUserForName(username);
			if(u != null) {
				u.setEmailAddress(email);
				s.update(u);
			} else {
				throw new RuntimeException("User not found");
			}
		} finally {
			tx.commit();
		}
	}
	
	@Override
	public void setAdministrator(String username, boolean isAdmin) {
		Session s = sessionProvider.get();
		Transaction tx = s.beginTransaction();
		try {
			HbmUserImpl u = getUserForName(username);
			if(u != null) {
				u.setAdministrator(isAdmin);
				s.update(u);
			} else {
				throw new RuntimeException("User not found");
			}
		} finally {
			tx.commit();
		}
	}
	
}
