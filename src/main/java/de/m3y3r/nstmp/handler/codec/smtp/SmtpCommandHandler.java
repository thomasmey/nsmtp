package de.m3y3r.nstmp.handler.codec.smtp;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import de.m3y3r.nstmp.handler.codec.smtp.model.MailTransaction;
import de.m3y3r.nstmp.handler.codec.smtp.model.SessionContext;
import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpCommandReply;
import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpReplyStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;

/**
 * RFC2821 SMTP server
 *
 * https://tools.ietf.org/html/rfc2821
 * @author thomas
 *
 */
public class SmtpCommandHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf frame = (ByteBuf) msg;

		if(!isSessionContext(ctx)) {
			/* we did receive an command before we send an initial greetings reply */
			/* what to do here? is it possible to send a reply here?
			 */
			return;
		}

		if(frame.readableBytes() < 4) {
			throw new UnknownCommandException("");
		}

		SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_KEY).get();

		CharSequence line = frame.readCharSequence(frame.readableBytes(), StandardCharsets.US_ASCII);
		CharSequence cmd = line.subSequence(0, 4);
		CharSequence argument = line.length() > 4 ? line.subSequence(4, line.length()) : null;

		validateCommand(cmd);
		validateCommandOrder(sessionContext, cmd);

		/* process command */

		/* 4.5.1 Minimum Implementation */
		switch (cmd.toString()) { //FIXME: compare by Charsequence if possible
		case "EHLO":
		{
			resetMailTransaction(sessionContext);
			CharSequence domainOrAddressLiteral = argument;
			String greeting = "Greetings from Netty SMTP server";
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, domainOrAddressLiteral + " " + greeting);
			ctx.writeAndFlush(reply);
			break;
		}

		case "HELO":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R502, "TODO");
			ctx.writeAndFlush(reply);
		}

		case "MAIL":
		{

			MailTransaction mailTx = new MailTransaction();
			mailTx.setFrom(argument);

			sessionContext.mailTransaction = mailTx;

			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			ctx.writeAndFlush(reply);
			break;
		}

		case "RCPT":
		{
			MailTransaction mailTx = sessionContext.mailTransaction;
			mailTx.addTo(argument);

			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			ctx.writeAndFlush(reply);
			break;
		}

		case "DATA":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R354, "Start mail input; end with <CRLF>.<CRLF>");
			ctx.writeAndFlush(reply);
			ctx.pipeline().replace(this, "smptInData", new SmtpDataHandler());
			break;
		}

		case "QUIT":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R221, null);
			ctx.writeAndFlush(reply);
			ctx.close();
			break;
		}

		/* order-independent commands:
		 * "The NOOP, HELP, EXPN, VRFY, and RSET commands can be used at any time during a session"
		 */
		case "RSET":
		{
			resetMailTransaction(sessionContext); // abort any ongoing mail transaction
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			ctx.writeAndFlush(reply);
			break;
		}
		case "VRFY":
		{
			boolean isValid = verifyUserOrMailbox(argument);
			Object reply;
			if(isValid)
				reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			else
				reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");

			ctx.writeAndFlush(reply);
			break;
		}
		case "EXPN":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R502, "TODO");
			ctx.writeAndFlush(reply);
			break;
		}
		case "HELP":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			ctx.writeAndFlush(reply);
			break;
		}
		case "NOOP":
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
			ctx.writeAndFlush(reply);
			break;
		}

		default:
		{
			Object reply = new SmtpCommandReply(SmtpReplyStatus.R500, "WAT?");
			ctx.writeAndFlush(reply);
			break;
		}
		}

		sessionContext.lastCmd = cmd;
	}

	private void resetMailTransaction(SessionContext ctx) {
		ctx.mailTransaction = null;
	}

	/**
	 * was this session started at all?
	 * @param ctx
	 * @return 
	 */
	private boolean isSessionContext(ChannelHandlerContext ctx) {
		Attribute<SessionContext> sessionStarted = ctx.channel().attr(SessionContext.ATTRIBUTE_KEY);
		return sessionStarted.get() != null;
	}

	private boolean verifyUserOrMailbox(CharSequence argument) {
		return false;
	}

	private static final CharSequence[] ALWAYS_ALLOWED_COMMANDS = new String[] {"HELO", "EHLO", "RSET"};

	/**
	 * is the command allowed in the current state
	 * @param ctx
	 * @param cmd
	 */
	private void validateCommandOrder(SessionContext ctx, CharSequence cmd) {
		if(Arrays.stream(ALWAYS_ALLOWED_COMMANDS).anyMatch(c -> CharSequenceComparator.equals(c, cmd)))
			return;

		if(ctx.lastCmd == null) {
			return;
		}

		// is a mail transaction going on already
		if(ctx.mailTransaction != null) {
			if(
					CharSequenceComparator.equals(cmd, "RCPT") && CharSequenceComparator.equals(ctx.lastCmd, "MAIL") ||
					CharSequenceComparator.equals(cmd, "RCPT") && CharSequenceComparator.equals(ctx.lastCmd, "RCPT") ||
					CharSequenceComparator.equals(cmd, "DATA") && CharSequenceComparator.equals(ctx.lastCmd, "RCPT")
					) {
				return;
			}
			throw new IllegalStateException();
		}
	}

	private static final CharSequence[] VALID_COMMANDS = new String[] {"HELO", "EHLO", "MAIL", "RSET", "VRFY", "EXPN", "NOOP", "QUIT"};

	/**
	 * is this an valid SMTP command?
	 * @param cmd
	 */
	private boolean validateCommand(CharSequence cmd) {
		return Arrays.stream(VALID_COMMANDS).anyMatch(c -> CharSequenceComparator.equals(c, cmd));
	}
}
