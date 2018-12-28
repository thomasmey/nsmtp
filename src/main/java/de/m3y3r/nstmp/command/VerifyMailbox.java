package de.m3y3r.nstmp.command;

import de.m3y3r.nstmp.model.MailTransaction;
import de.m3y3r.nstmp.model.SessionContext;
import de.m3y3r.nstmp.model.SmtpCommandReply;
import de.m3y3r.nstmp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;

public class VerifyMailbox implements SmtpCommand {

	@Override
	public CharSequence getCommandWord() {
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
