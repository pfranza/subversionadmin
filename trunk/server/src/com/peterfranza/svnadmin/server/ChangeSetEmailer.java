package com.peterfranza.svnadmin.server;

import java.util.List;

import com.peterfranza.svnadmin.server.ChangeMiner.ChangeSummary;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class ChangeSetEmailer {

	public static void sendChangeSetEmail(long revisionNumber) {

		try {

			ChangeSummary set = ChangeMiner.getSummary(ApplicationProperties
					.getProperty("repository_url"), ApplicationProperties
					.getProperty("repository_username"), ApplicationProperties
					.getProperty("repository_password"), revisionNumber);

			String subject = "svn commit: " + set.getMessage();
			MailSender.sendMail(ApplicationProperties
					.getProperty("smtp_server"), getFromAddress(set.getAuthor(), ApplicationProperties
					.getProperty("default_from_email")), getToAddresses(
					set.getChangeSet()), subject.length() < 100 ? subject
					: (subject.substring(0, 100) + "..."), set.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getFromAddress(String author,	String def) {
		String email = ACLOperationsDelegate.getInstance().getEmailForUser(author);
		return email != null ? email : def;
	}

	private static String[] getToAddresses(List<String> changes) {

		List<String> addresses = ACLOperationsDelegate.getInstance().getAddressesSubscribedTo(changes);
		return addresses.toArray(new String[addresses.size()]);
	}

	public static void main(String[] args) {
		sendChangeSetEmail(Long.valueOf(args[0]));
	}

}
