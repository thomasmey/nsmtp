package de.m3y3r.nsmtp.handler.codec.smtp;

import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import de.m3y3r.nsmtp.model.SmtpReplyStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * rfc5321 SMTP server - Handle data lines
 *
 * https://tools.ietf.org/html/rfc5321
 * @author thomas
 *
 */
public class SmtpDataHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf bbLine = (ByteBuf) msg;
		SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_KEY).get();

		transformLine(bbLine);

		// is the line a single dot i.e. end of DATA?
		if(bbLine.readableBytes() == 1 && bbLine.getByte(bbLine.readerIndex()) == '.') {
			//if so, switch back to command handler
			ctx.pipeline().replace(this, "smptInCommand", new SmtpCommandHandler());

			boolean rc = sessionContext.mailTransaction.mailFinished();
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
			sessionContext.mailTransaction.addDataLine(bbLine.copy()); //TODO: copy or retain?!
		}
	}

	private void transformLine(ByteBuf line) {
		if(line.readableBytes() >= 1 && line.getByte(line.readerIndex()) == '.') {
			if(line.readableBytes() > 1) {
				line.readByte(); // consume '.' if there are other characters on the line
			}
		}
	}
}
