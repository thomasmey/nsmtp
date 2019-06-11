package de.m3y3r.nsmtp;

import java.io.File;

import de.m3y3r.nsmtp.maildata.MailDataProcessor;
import de.m3y3r.nsmtp.maildata.MailDirWriter;

public enum Config {

	INSTANCE;

	public int getDefaultDataLen() {
		return 1001; // 1000 or 1001 for extra .. -> . handling?
	}

	public int getDefaultCommandLen() {
		return 512;
	}

	public String getDomain() {
		return "localhost";
	}

	public File getMailDir() {
		return new File(System.getProperty("user.home"), "nsmtp");
	}

	public MailDataProcessor getMailDataProcessor() {
//		return new InMemoryMailDataProcessor();
		return new MailDirWriter();
	}

	public String getTlsKeyFile() {
		return null;
	}

	public String getTlsTrustStoreFile() {
		return null;
	}

	public String getTlsKeyPassword() {
		return null;
	}
}
