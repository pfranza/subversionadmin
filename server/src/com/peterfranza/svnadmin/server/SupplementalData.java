package com.peterfranza.svnadmin.server;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SupplementalData {

	public static class User {
		
		public static enum ACCESS_LEVEL {USER, ADMIN};
		
		private ACCESS_LEVEL accessLevel = ACCESS_LEVEL.USER; 		
		private String username;
		private String emailAddress;
		private List<String> subscriptions = new ArrayList<String>();

		public final String getUsername() {
			return username;
		}

		public final void setUsername(String username) {
			this.username = username;
		}

		public final String getEmailAddress() {
			return emailAddress;
		}

		public final void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}

		public final void addSubscription(String subscription) {
			subscriptions.add(subscription);
		}

		public final void removeSubscriptions(String subscription) {
			subscriptions.remove(subscription);
		}

		public final List<String> getSubscriptions() {
			return subscriptions;
		}

		public final void setSubscriptions(List<String> subscriptions) {
			this.subscriptions = subscriptions;
		}
		
		public final void setAccessLevel(ACCESS_LEVEL l) {
			accessLevel = l;
		}
		
		public final ACCESS_LEVEL getAccessLevel() {
			return accessLevel;
		}

	}

	private static SupplementalData instance;

	private List<User> users = new ArrayList<User>();
	
	public List<User> getUsers() {
		return users;
	}

	public void save() throws FileNotFoundException {
		XMLEncoder encoder = new XMLEncoder(new FileOutputStream(
				ApplicationProperties.getProperty("persistant_datafile")));
		encoder.writeObject(users);
		encoder.close();
	}

	@SuppressWarnings("unchecked")
	public void load() throws FileNotFoundException {
		File file = new File(ApplicationProperties.getProperty("persistant_datafile"));
		if(file.exists()) {
			XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
			try {
				users = (List<User>) decoder.readObject();
			} catch (Exception e) {
			}
//			System.out.println("Loaded " + users.size() + " users");
		}
	}

	private SupplementalData() {
	}

	public static SupplementalData getInstance() throws FileNotFoundException {
		if(instance == null) {
			instance = new SupplementalData();
			instance.load();
		}
		return instance;
	}

	public String getUserEmailAddress(String username) {
		for(User u: users) {
			if(u.getUsername().equalsIgnoreCase(username)) {
				return u.getEmailAddress();
			}
		}
		return "";
	}
}
