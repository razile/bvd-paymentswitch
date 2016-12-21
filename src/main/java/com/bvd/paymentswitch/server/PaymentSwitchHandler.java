 package com.bvd.paymentswitch.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.FuelCode;
import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.processing.client.AuthorizationClient;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;
import com.bvd.paymentswitch.processing.provider.ProviderFactory;
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
	
	@Autowired
	private ProviderFactory providerFactory;
	
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
			PosAuthorization request = new PosAuthorization(message);
			authService.saveAuthorization(request);		

			// find processor 
			String bin = request.getCard1().substring(0,6);
			ProcessingProvider provider = providerFactory.getProvider(bin);
			
			if (provider == null) {
				sendErrorResponse(ctx, request, "Unable to process card");
			} else if (provider.getPaymentProcessor().getName().equalsIgnoreCase("COMDATA") && (request.getUnitNumber() == null || request.getUnitNumber().length() == 0)) {
				sendUnitPromptResponse(ctx, request);
			} else {
				// authorize it
				String merchantCode = authService.findMerchantID(request.getSiteId().trim(), provider.getPaymentProcessor().getId());
				
				if (merchantCode == null || merchantCode.isEmpty()) {
					sendErrorResponse(ctx, request, "Unknown merchant");
				} else {
					FuelCode fc = authService.findFuelCode(request.getFuelType());
					request.setFuelCode(fc);
					try {
						authClient.authorize(request, provider, merchantCode, ctx);
					} catch (Exception e) {
						logger.debug(e.getMessage());
						sendErrorResponse(ctx, request, "Error processing card");
					}
				}
			}
		
		} else {
		
			logger.info("Check Digit Failure, sending NAK");
			// check digits do not match, send back a NAK
			ctx.write(String.valueOf(ASCIIChars.NAK));

	 		// close the channel once the content is fully written
	    	ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	
	public void sendUnitPromptResponse(ChannelHandlerContext ctx, PosAuthorization request) {
		PosAuthorization response = new PosAuthorization(request);
		response.setResponseFlags(request);
		response.setDenied("00020","Unit Number Required");
		response.addPrompt("M2", "L,X6");
		
		ctx.write(response.toString());
 		// close the channel once the content is fully written
    	ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
	
	
	public void sendErrorResponse(ChannelHandlerContext ctx, PosAuthorization request, String error) {
		PosAuthorization response = new PosAuthorization(request);
		response.setResponseFlags(request);
		response.setDenied(error, null);
		
		ctx.write(response.toString());
 		// close the channel once the content is fully written
    	ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error(cause.getMessage());
		ctx.write(String.valueOf(ASCIIChars.NAK));
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}

	

}
