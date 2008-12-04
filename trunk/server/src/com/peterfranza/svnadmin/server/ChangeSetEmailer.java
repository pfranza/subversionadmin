package com.peterfranza.svnadmin.server;

import com.peterfranza.svnadmin.server.ChangeMiner.ChangeSummary;

public class ChangeSetEmailer {

	public static void sendChangeSetEmail(long revisionNumber, String[] toAddresses, String from) {
		ChangeSummary set = ChangeMiner.getSummary(ApplicationProperties
				.getProperty("repository_url"), ApplicationProperties
				.getProperty("repository_username"), ApplicationProperties
				.getProperty("repository_password"), revisionNumber);

		String subject = "svn commit: " + set.getMessage();
		MailSender.sendMail(ApplicationProperties.getProperty("smtp_server"),
				from, toAddresses,
				subject.length() < 100 ? subject
						: (subject.substring(0, 100) + "..."), set.toString());
	}
	
	
	public static void main(String[] args) {
		String[] to = { "pfranza@gmail.com" };
		sendChangeSetEmail(Long.valueOf(args[0]), to, "pfranza@home.com");

	}
	

}
