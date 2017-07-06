package com.bvd.paymentswitch.server;

import java.util.concurrent.TimeUnit;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.bvd.paymentswitch.utils.ASCIIChars;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

@Component
@Qualifier("paymentSwitchInitializer")
public class PaymentSwitchInitializer extends ChannelInitializer<SocketChannel> {

	static final Logger logger = LoggerFactory.getLogger(PaymentSwitchInitializer.class);
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
	
	@Value("${pos.timeout.seconds}")
	private long timeout;
	
	@Autowired
	private Provider<PaymentSwitchHandler> paymentSwitchHandlerProvider;

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		logger.debug("Initializaing handler for channel: " + ch.toString());
		ChannelPipeline p = ch.pipeline();

		if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}

		p.addLast(new ReadTimeoutHandler(timeout, TimeUnit.SECONDS));
		
		p.addLast(new LoggingHandler("netty", LogLevel.DEBUG));
		// Add protocol decoders / encoders

		// the first decoder will look for the <ETX> end of text ASCII char.
		// This will produce a substring starting with <STX>
		p.addLast(new DelimiterBasedFrameDecoder(32768, ASCIIChars.ETX_DELIMITER));

		// The STX/ETX codecs will ensure the message is wrapped/stripped of
		// <STX> start and <ETX> end chars
		p.addLast(DECODER);
		p.addLast(ENCODER);

		// add the handler class containing processing business logic
		p.addLast(paymentSwitchHandler());
	}
	
	
	public PaymentSwitchHandler paymentSwitchHandler() {
		return paymentSwitchHandlerProvider.get();
	}
}
