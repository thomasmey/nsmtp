package de.m3y3r.nsmtp.command;

import de.m3y3r.nsmtp.model.MailTransaction;
import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import de.m3y3r.nsmtp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;

public class Mail extends AbstractSmtpCommand {

	@Override
	public CharSequence getCommandVerb() {
		return "MAIL";
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {
		MailTransaction mailTx = new MailTransaction();
		mailTx.setFrom(argument);

		/*
		 * TODO: process new optional parameter from 8bit-MIMEtransport (rfc1652):
		 *   "7BIT" / "8BITMIME"
		 * and add it to the mailTransaction
		 */
		ctxMailSession.mailTransaction = mailTx;
		SmtpCommandReply reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
		return reply;
	}
}
