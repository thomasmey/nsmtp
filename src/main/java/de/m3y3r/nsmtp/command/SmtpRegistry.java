package de.m3y3r.nsmtp.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		commands = new HashMap<>();

		ServiceLoader<SmtpCommand> sl = ServiceLoader.load(SmtpCommand.class);
		sl.forEach(s -> commands.put(s.getCommandVerb().toString(), s));
	}

	public SmtpCommand getCommand(String command) {
		return commands.get(command);
	}

	public List<SmtpCommand> getCommands() {
		return new ArrayList<>(commands.values());
	}
}
