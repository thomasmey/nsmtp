package de.m3y3r.nstmp.model;

import java.util.ArrayList;
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
		return false;
	}

}
