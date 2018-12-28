package de.m3y3r.nstmp.command;

import java.util.Map;
import java.util.ServiceLoader;

/**
 * https://www.iana.org/assignments/mail-parameters/mail-parameters.xhtml
 * 
 * @author thomas
 *
 */
public enum SmtpRegistry {

	INSTANCE;

	private Map<String, SmtpCommand> commands;

	private SmtpRegistry() {
		ServiceLoader<SmtpCommand> sl = ServiceLoader.load(SmtpCommand.class);
		sl.forEach(s -> commands.put(s.getCommandWord().toString(), s));
	}

	public SmtpCommand getCommand(String command) {
		return commands.get(command);
	}
}
