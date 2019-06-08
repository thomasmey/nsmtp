package de.m3y3r.nsmtp.command;

import java.util.List;

import de.m3y3r.nsmtp.model.MailTransaction;
import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import de.m3y3r.nsmtp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;

public class VerifyMailbox extends AbstractSmtpCommand {

	@Override
	public CharSequence getCommandVerb() {
		return "VRFY";
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {

		boolean isValid = verifyUserOrMailbox(argument);
		SmtpCommandReply reply;
		if(isValid)
			reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
		else
			reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");

		return reply;
	}

	private boolean verifyUserOrMailbox(CharSequence argument) {
		return true;
	}
}
