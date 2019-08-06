package de.m3y3r.nsmtp.maildata.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

public class FirebaseMsgMDP extends InMemoryMailDataProcessor {

	private static Logger logger = LoggerFactory.getLogger(FirebaseMsgMDP.class);

	static {
		try {
			String databaseUrl = System.getenv("FIREBASE_DATABASE_URL");
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.getApplicationDefault())
					.setDatabaseUrl(databaseUrl)
					.build();
			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			logger.error("failed to init firebase {}", e);
		}
	}

	@Override
	public boolean finish() {
		String registrationToken = System.getenv("FIREBASE_REGISTRATION_TOKEN");

		CharSequence subject = this.getMailData().stream().filter(cs -> cs.toString().startsWith("Subject: ")).map(cs -> cs.subSequence("Subject: ".length(), cs.length())).findFirst().orElse("MISSING");
		CharSequence date = this.getMailData().stream().filter(cs -> cs.toString().startsWith("Date: ")).map(cs -> cs.subSequence("Date: ".length(), cs.length())).findFirst().orElse("MISSING");

		Message message = Message.builder()
				.putData("subject", subject.toString())
				.putData("date", date.toString())
				.setToken(registrationToken)
				.build();

		try {
			String msgId = FirebaseMessaging.getInstance().send(message);
			logger.info("did send msg {}", msgId);
		} catch (FirebaseMessagingException e) {
			logger.error("failed to send msg {}", e);
		} finally {}

		return true;
	}

}
