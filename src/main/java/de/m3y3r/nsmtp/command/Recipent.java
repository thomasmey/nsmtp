package de.m3y3r.nsmtp.command;

import java.util.List;

import de.m3y3r.nsmtp.handler.codec.smtp.SmtpDataHandler;
import de.m3y3r.nsmtp.model.MailTransaction;
import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import de.m3y3r.nsmtp.model.SmtpReplyStatus;
import de.m3y3r.nsmtp.util.Path;
import io.netty.channel.ChannelHandlerContext;

public class Recipent extends AbstractSmtpCommand {

	@Override
	public CharSequence getCommandVerb() {
		return "RCPT";
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {

		Path.parse(argument);
		MailTransaction mailTx = ctxMailSession.mailTransaction;
		mailTx.addTo(argument);

		SmtpCommandReply reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
		return reply;
	}
}
