package de.m3y3r.nstmp.command;

import java.util.List;

import de.m3y3r.nstmp.handler.codec.smtp.SmtpDataHandler;
import de.m3y3r.nstmp.model.MailTransaction;
import de.m3y3r.nstmp.model.SessionContext;
import de.m3y3r.nstmp.model.SmtpCommandReply;
import de.m3y3r.nstmp.model.SmtpReplyStatus;
import de.m3y3r.nstmp.util.Path;
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
