package com.kate.smtp;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.search.FlagTerm;

public class Main {

	public static void main(String[] args) {

		Properties props = new Properties();
		Properties creds = new Properties();

		try {
			//Loading properties
			props.load(Main.class.getResourceAsStream("/smtp.properties"));
			creds.load(Main.class.getResourceAsStream("/user.properties"));

			//Create session with our credentials
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("smtp.gmail.com", creds.getProperty("email"),
					creds.getProperty("password"));

			//Open Inbox folder in read-only mode
			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);

			//Create unseen search filter
			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm searchFilter = new FlagTerm(seen, false);
			//Getting all messages by filter
			Message messages[] = inbox.search(searchFilter);

			//Search message by subject
			System.out.println("------------------------------");
			Message specMsg = null;
			for (int i = 0; i < messages.length; i++) {
				if (messages[i].getSubject().equalsIgnoreCase(
						"Test Unread Email")) {
					specMsg = messages[i];
					break;
				}
			}
			//Printing result
			if (specMsg != null) {
				System.out.println("Mail found ");
				System.out.println("From : " + specMsg.getFrom()[0]);
				System.out.println("Subject : " + specMsg.getSubject());
				System.out.println("Sent Date : " + specMsg.getSentDate());
				System.out.println("Sent Date : " + specMsg.getContent());
			} else {
				System.out.println("Mail not found ");
			}

			// Close Mail folder
			inbox.close(true);
			store.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}