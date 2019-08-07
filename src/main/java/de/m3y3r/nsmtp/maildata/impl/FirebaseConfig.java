package de.m3y3r.nsmtp.maildata.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseConfig {

	private static FirebaseConfig instance;

	private static Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

	public synchronized static FirebaseConfig getInstance() {
		if(instance == null) {
			instance = new FirebaseConfig();
		}
		return instance;
	}

	private FirebaseConfig() {
		try {
			String databaseUrl = getDatabaseUrl();
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(getServiceAccountCredsIS()))
					.setDatabaseUrl(databaseUrl)
					.build();
			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			logger.error("failed to init firebase {}", e);
		}
	}

	private InputStream getServiceAccountCredsIS() throws FileNotFoundException {
		String value = System.getProperty("firebase.serviceaccount.path", System.getenv("firebase.serviceaccount.path"));
		return new FileInputStream(value);
	}

	public String getDatabaseUrl() {
		String value = System.getProperty("firebase.database.url", System.getenv("firebase.database.url"));
		return value;
	}

	public String getRegistrationToken(Object ctx) {
		String value = System.getProperty("firebase.registration.token", System.getenv("firebase.registration.token"));
		return value;
	}

}
