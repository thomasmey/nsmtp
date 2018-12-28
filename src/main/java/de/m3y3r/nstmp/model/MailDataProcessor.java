package de.m3y3r.nstmp.model;

public interface MailDataProcessor {

	void addDataLine(CharSequence argument);

	boolean finish();

}
