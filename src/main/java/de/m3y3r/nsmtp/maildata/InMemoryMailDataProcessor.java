package de.m3y3r.nsmtp.maildata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		return true;
	}

	public List<CharSequence> getMailData() {
		return Collections.unmodifiableList(mailData);
	}
}
