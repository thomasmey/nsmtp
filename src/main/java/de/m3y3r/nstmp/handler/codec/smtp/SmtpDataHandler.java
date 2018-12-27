package de.m3y3r.nstmp.handler.codec.smtp;

import java.nio.charset.StandardCharsets;

import de.m3y3r.nstmp.handler.codec.smtp.model.SessionContext;
import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpCommandReply;
import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpReplyStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * RFC2821 SMTP server - Handle data lines
 *
 * https://tools.ietf.org/html/rfc2821
 * @author thomas
 *
 */
public class SmtpDataHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf frame = (ByteBuf) msg;
		CharSequence line = frame.readCharSequence(frame.readableBytes(), StandardCharsets.US_ASCII);

		SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_KEY).get();

		// TODO: implement "4.5.2 Transparency"
		CharSequence transformedLine = transformLine(line);
		if(CharSequenceComparator.equals(".", transformedLine)) {
			ctx.pipeline().replace(this, "smptInCommand", new SmtpCommandHandler());

			boolean rc = processMail(sessionContext);
			//FIXME: reset mailtransaction?!
			sessionContext.mailTransaction = null;
			if(rc) {
				Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
				ctx.writeAndFlush(reply);
			} else {
				Object reply = new SmtpCommandReply(SmtpReplyStatus.R450, "FAILED");
				ctx.writeAndFlush(reply);
			}
		} else {
			sessionContext.mailTransaction.addBodyLine(transformedLine);
		}
	}

	private boolean processMail(SessionContext sessionContext) {
		return true;
	}

	private CharSequence transformLine(CharSequence line) {
		return line;
	}
}
