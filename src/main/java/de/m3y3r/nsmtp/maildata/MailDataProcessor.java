package de.m3y3r.nsmtp.maildata;

public interface MailDataProcessor {

	void addDataLine(CharSequence argument);

	/**
	 * callback being called when the last mail data line was received, i.e. 
	 * an single dot on a line
	 * @return true, if mail was processed successfully, false if some error did happen
	 *  in that case the mail will be replied with an error code
	 */
	boolean finish();

}
