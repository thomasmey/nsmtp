package de.m3y3r.nsmtp.model;

public interface MailDataProcessor {

	void addDataLine(CharSequence argument);

	boolean finish();

}
