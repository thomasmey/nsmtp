package de.m3y3r.nsmtp.maildata.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.m3y3r.nsmtp.maildata.MailDataProcessor;
import de.m3y3r.nsmtp.model.imf.InternetMessageBuilder;

public class InMemoryMailDataProcessor implements MailDataProcessor {

	private List<CharSequence> mailData;

	public InMemoryMailDataProcessor() {
		this.mailData = new ArrayList<CharSequence>();
	}

	@Override
	public void addDataLine(CharSequence argument) {
		mailData.add(argument);
	}

	@Override
	public boolean finish() {
		InternetMessageBuilder builder = new InternetMessageBuilder();
		for(CharSequence line : mailData) {
			builder.addLine(line);
		}
		builder.build();
		return true;
	}

	public List<CharSequence> getMailData() {
		return Collections.unmodifiableList(mailData);
	}
}
