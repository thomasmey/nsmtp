package de.m3y3r.nstmp.handler.codec.smtp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * models an ongoing mail transaction
 * @author thomas
 *
 */
public class MailTransaction {

	private CharSequence domain; // HELO
	private CharSequence reversePath; // MAIL command (1 time) - i.e. FROM
	private List<CharSequence> forwardPath = new ArrayList<>(); //RCPT commands (n times, should be limited to 100?) - i.e. TO
	private List<CharSequence> mailData = new ArrayList<>();

	public void addTo(CharSequence argument) {
		if(argument == null) {
			throw new IllegalArgumentException();
		}
		forwardPath.add(argument);
	}

	public void setFrom(CharSequence argument) {
		if(argument == null) {
			throw new IllegalArgumentException();
		}

		if(this.reversePath != null) {
			throw new IllegalStateException();
		}

		this.reversePath = argument;
	}

	public void addBodyLine(CharSequence argument) {
		if(argument == null) {
			throw new IllegalArgumentException();
		}

		this.mailData.add(argument);
	}

}
