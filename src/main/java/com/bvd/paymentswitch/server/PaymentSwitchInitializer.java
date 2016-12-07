package com.bvd.paymentswitch.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.utils.ASCIIChars;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

@Component
@Qualifier("paymentSwitchInitializer")
public class PaymentSwitchInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	@Qualifier("sslContext")
	private SslContext sslCtx;

	@Autowired
	@Qualifier("decoder")
	private StringDecoder DECODER;

	@Autowired
	@Qualifier("encoder")
	private StringEncoder ENCODER;

	@Autowired
	@Qualifier("paymentSwitchHandler")
	private ChannelInboundHandlerAdapter paymentSwitchHandler;

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();

		if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}

		// Add protocol decoders / encoders

		// the first decoder will look for the <ETX> end of text ASCII char.
		// This will produce a substring starting with <STX>
		p.addLast(new DelimiterBasedFrameDecoder(32768, ASCIIChars.ETX_DELIMITER));

		// The STX/ETX codecs will ensure the message is wrapped/stripped of
		// <STX> start and <ETX> end chars
		p.addLast(DECODER);
		p.addLast(ENCODER);

		// add the handler class containing processing business logic
		p.addLast(paymentSwitchHandler);
	}
}