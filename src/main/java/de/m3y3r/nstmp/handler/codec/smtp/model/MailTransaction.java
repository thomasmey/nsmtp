package de.m3y3r.nstmp.handler.codec.smtp.model;

import java.util.ArrayList;
import java.util.List;

import io.netty.util.AsciiString;

/**
 * models an ongoing mail transaction
 * @author thomas
 *
 */
public class MailTransaction {

	private String domain; // HELO
	private String reversePath; // MAIL command (1 time) - i.e. FROM
	private List<String> forwardPath = new ArrayList<>(); //RCPT commands (n times, should be limited to 100?) - i.e. TO
	private List<String> mailData = new ArrayList<>();

	public void addTo(String argument) {
		if(argument == null) {
			throw new IllegalArgumentException();
		}
		forwardPath.add(argument);
	}

	public void setFrom(String argument) {
		if(argument == null) {
			throw new IllegalArgumentException();
		}

		if(this.reversePath != null) {
			throw new IllegalStateException();
		}

		this.reversePath = argument;
	}

	public void addBodyLine(String argument) {
		if(argument == null) {
			throw new IllegalArgumentException();
		}

		this.mailData.add(argument);
	}

}
