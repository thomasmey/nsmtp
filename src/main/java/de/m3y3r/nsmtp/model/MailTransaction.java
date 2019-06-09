package de.m3y3r.nsmtp.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.m3y3r.nsmtp.Config;
import de.m3y3r.nsmtp.maildata.MailDataProcessor;
import de.m3y3r.nsmtp.util.Path;

/**
 * models an ongoing mail transaction
 * @author thomas
 *
 */
public class MailTransaction {

	private static final Logger logger = LoggerFactory.getLogger(MailTransaction.class);

	private Path reversePath; // MAIL command (1 time) - i.e. FROM
	private List<Path> forwardPath = new ArrayList<>(); //RCPT commands (n times, should be limited to 100?) - i.e. TO

	private MailDataProcessor mailDataProcessor;

	public MailTransaction() {
		logger.info("Begin new MailTransaction");
		mailDataProcessor = Config.INSTANCE.getMailDataProcessor();
	}

	public void addTo(CharSequence argument) {
		if(argument == null) {
			throw new IllegalArgumentException();
		}
		forwardPath.add(Path.parse(argument));
	}

	public void setFrom(CharSequence argument) {
		if(argument == null) {
			throw new IllegalArgumentException();
		}

		if(this.reversePath != null) {
			throw new IllegalStateException();
		}

		this.reversePath = Path.parse(argument);
	}

	public void addDataLine(CharSequence argument) {
		if(argument == null) {
			throw new IllegalArgumentException();
		}

		mailDataProcessor.addDataLine(argument);
	}

	public boolean mailFinished() {
		return mailDataProcessor.finish();
	}

}
