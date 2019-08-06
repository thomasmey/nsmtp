package de.m3y3r.nsmtp.maildata.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.m3y3r.nsmtp.Config;

public class MailDirWriter extends InMemoryMailDataProcessor {

	private static Logger log = LoggerFactory.getLogger(MailDirWriter.class);

	public MailDirWriter() {
		super();
		log.info("going to use mail dir {}", Config.INSTANCE.getMailDir());
	}

	@Override
	public boolean finish() {
		try {
			File mailFile = File.createTempFile("mail-", ".raw", Config.INSTANCE.getMailDir());
			try(FileOutputStream fos = new FileOutputStream(mailFile);
				OutputStreamWriter w = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
				for(CharSequence line: getMailData()) {
					w.append(line);
					w.append('\n'); // new line
				}
			}
			return super.finish();
		} catch (IOException e) {
			log.error("Failed to create new mail file", e);
		}
		return false;
	}

}
