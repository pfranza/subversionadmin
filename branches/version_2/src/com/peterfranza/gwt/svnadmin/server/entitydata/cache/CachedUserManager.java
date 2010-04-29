package com.peterfranza.gwt.svnadmin.server.entitydata.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class CachedUserManager implements UserManager{

	private final UserManager base;
	private final long maxAge;

	private Map<String, CacheEntry<User>> userCache = new HashMap<String, CacheEntry<User>>();
	private CacheEntry<Collection<? extends User>> setCache;
	
	public CachedUserManager(UserManager base, long maxAge) {
		this.base = base;
		this.maxAge = maxAge;
	}
	
	@Override
	public boolean authenticate(String username, String password) {
		return base.authenticate(username, password);
	}

	@Override
	public void createUser(String username) {
		base.createUser(username);
		invalidate();
	}

	@Override
	public User getUserForName(String username) {
		CacheEntry<User> c = userCache.get(username);
		if(c != null && c.isValid(maxAge)) {
			return c.object;
		}
		
		User v = base.getUserForName(username);
		userCache.put(username, new CacheEntry<User>(v));
		return v;
	}

	@Override
	public Collection<? extends User> getUsers() {
		if(setCache != null && setCache.isValid(maxAge)) {
			return setCache.object;
		}
		
		Collection<? extends User> v = base.getUsers();
		setCache = new CacheEntry<Collection<? extends User>>(v);
		return v;
	}

	@Override
	public boolean isMutable() {
		return base.isMutable();
	}

	@Override
	public void removeUser(String username) {
		base.removeUser(username);
		invalidate();
	}

	@Override
	public void setAdministrator(String username, boolean isAdmin) {
		base.setAdministrator(username, isAdmin);
		invalidate();
	}

	@Override
	public void setEmailAddress(String username, String email) {
		base.setEmailAddress(username, email);
		invalidate();
	}

	@Override
	public void setPassword(String username, String password) {
		base.setPassword(username, password);
		invalidate();
	}
	
	public void invalidate() {
		setCache = null;
		userCache.clear();
	}
	
	private static class CacheEntry<T> {
		
		public CacheEntry(T obj) {
			this.object = obj;
			this.entryTime = System.currentTimeMillis();
		}
		
		private long entryTime;
		T object;
		
		public boolean isValid(long maxAge) {
			return System.currentTimeMillis() - entryTime <= maxAge;
		}
	}

}
