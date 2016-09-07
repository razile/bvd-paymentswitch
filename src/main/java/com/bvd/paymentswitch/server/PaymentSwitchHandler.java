package com.bvd.paymentswitch.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.bvd.paymentswitch.authorization.client.AuthorizationClient;
import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.KardallHostAuthorization;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Qualifier("paymentSwitchHandler")
@ChannelHandler.Sharable
public class PaymentSwitchHandler extends SimpleChannelInboundHandler<String> {
	static final Logger logger = LoggerFactory.getLogger(PaymentSwitchHandler.class);   
	static final Logger requestLogger = LoggerFactory.getLogger("Request-Logger");
	@Autowired
	@Qualifier("authorizationClient")
	private AuthorizationClient authClient;
	
	@Autowired
	private AuthorizationService authService;
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String message) {
		
		logger.debug("READ: " + message);
		
		// validate request using check digit
		if (ProtocolUtils.checkDigit(message)) {
			
			requestLogger.info(message);
			
			// parse the request
			KardallHostAuthorization request = new KardallHostAuthorization(message);
			request = authService.saveAuthorization(request);		

			// find processor 
			String bin = request.getCard1().substring(0,6);
			PaymentProcessor p = authService.findPaymentProcessor(Integer.parseInt(bin));	
			
			// authorize it
			authClient.authorize(request, p, ctx);
		
		} else {
		
			logger.info("Check Digit Failure, sending NAK");
			// check digits do not match, send back a NAK
			ctx.write(String.valueOf(ASCIIChars.NAK));

	 		// close the channel once the content is fully written
	    	ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error(cause.getMessage());
		ctx.write(String.valueOf(ASCIIChars.NAK));
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}

	

}
