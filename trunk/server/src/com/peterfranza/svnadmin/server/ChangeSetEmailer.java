package com.peterfranza.svnadmin.server;

import java.util.ArrayList;
import java.util.List;

import com.peterfranza.svnadmin.server.ChangeMiner.ChangeSummary;
import com.peterfranza.svnadmin.server.SupplementalData.User;

public class ChangeSetEmailer {

	public static void sendChangeSetEmail(long revisionNumber) {

		try {
			SupplementalData data = SupplementalData.getInstance();
			ChangeSummary set = ChangeMiner.getSummary(ApplicationProperties
					.getProperty("repository_url"), ApplicationProperties
					.getProperty("repository_username"), ApplicationProperties
					.getProperty("repository_password"), revisionNumber);

			String subject = "svn commit: " + set.getMessage();
			MailSender.sendMail(ApplicationProperties
					.getProperty("smtp_server"), getFromAddress(data, set
					.getAuthor(), ApplicationProperties
					.getProperty("default_from_email")), getToAddresses(
					data, set.getChangeSet()), subject.length() < 100 ? subject
					: (subject.substring(0, 100) + "..."), set.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getFromAddress(SupplementalData data, String author,
			String def) {
		for (User u : data.getUsers()) {
			if (u.getUsername().equalsIgnoreCase(author)) {
				return u.getEmailAddress();
			}
		}
		return def;
	}

	private static String[] getToAddresses(SupplementalData data,
			List<String> changes) {

		ArrayList<String> addresses = new ArrayList<String>();
		for (User u : data.getUsers()) {
			if (isSubscribedTo(u, changes)) {
				addresses.add(u.getEmailAddress());
			}
		}

		return addresses.toArray(new String[addresses.size()]);
	}

	private static boolean isSubscribedTo(User u, List<String> changes) {
		for (String subscription : u.getSubscriptions()) {
			for (String change : changes) {
				if (change.startsWith(subscription)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		sendChangeSetEmail(Long.valueOf(args[0]));
	}

}
