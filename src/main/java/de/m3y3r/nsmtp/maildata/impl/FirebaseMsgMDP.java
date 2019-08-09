package de.m3y3r.nsmtp.maildata.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

public class FirebaseMsgMDP extends InMemoryMailDataProcessor {

	private static Logger logger = LoggerFactory.getLogger(FirebaseMsgMDP.class);

	@Override
	public boolean finish() {
		Object ctx = null;
		String registrationToken = FirebaseConfig.getInstance().getRegistrationToken(ctx);

		CharSequence subject = this.getMailData().stream().filter(cs -> cs.toString().startsWith("Subject: ")).map(cs -> cs.subSequence("Subject: ".length(), cs.length())).findFirst().orElse("MISSING");

		Notification notification = new Notification("New transaction event DB", subject.toString());
		Message message = Message.builder()
				.setNotification(notification)
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
