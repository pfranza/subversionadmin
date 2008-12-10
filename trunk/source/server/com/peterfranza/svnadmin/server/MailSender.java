package com.peterfranza.svnadmin.server;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

	public static void sendMail(String smtpServer, String fromAddress,
			String[] recipients, String subject, String body) {
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpServer);
		Session session = Session.getInstance(props, null);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromAddress));
			for (String to : recipients) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						to));
			}
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(body);
			Transport.send(msg);
		} catch (MessagingException mex) {
			System.err.println("send failed, exception: " + mex);
		}
	}

}
