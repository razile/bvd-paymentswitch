 package com.bvd.paymentswitch.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.FuelCode;
import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.processing.client.AuthorizationClient;
import com.bvd.paymentswitch.processing.client.AuthorizationFuture;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;
import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Qualifier("paymentSwitchHandler")
@ChannelHandler.Sharable
public class PaymentSwitchHandler extends SimpleChannelInboundHandler<String> {
	static final Logger logger = LoggerFactory.getLogger(PaymentSwitchHandler.class);   
	static final Logger requestLogger = LoggerFactory.getLogger("Request-Logger");
	
	@Autowired
	private Provider<AuthorizationClient> authClientProvider;
	
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
			PosAuthorization request = new PosAuthorization(message);
			authService.saveAuthorization(request);		

			// find processor 
			String bin = request.getCard1().substring(0,6);
			ProcessingProvider provider = authService.getProcessingProvider(bin);
			
			
			if (provider == null) {
				sendErrorResponse(ctx, request, "Unable to process card");
			} else if (provider.validatePOSRequest(request) == false) {
				sendRequiredPromptResponse(ctx, request, provider);
			} else {
				// authorize it
				String merchantId = authService.findMerchantID(request.getSiteId().trim(), provider.getPaymentProcessor().getId());
				
				if (merchantId == null || merchantId.isEmpty()) {
					sendErrorResponse(ctx, request, "Unknown merchant");
				} else {
					FuelCode fc = authService.findFuelCode(request.getFuelType());
					request.setFuelCode(fc);
					try {
						AuthorizationClient client = authorizationClient(provider); 
						client.connect(request, provider);
						
						ProcessorAuthorization processorRequest = provider.createProcessorRequest(request, merchantId);

						// provider.saveProcessorAuthorization(processorRequest);
						// write the request
						String requestMsg = provider.formatProcessorRequest(processorRequest);
						AuthorizationFuture authFuture = client.authorize(requestMsg);
						
						// while (!authFuture.isDone()) {
						// 	Thread.sleep(10);
						// }
						authFuture.await();
						
						String resp = authFuture.get();
					
						// logger.debug("Response ready to write...");
						ctx.write(resp);
				 		// close the channel once the content is fully written
				    	ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
				    	
				    	client.close();
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
	
	
	public AuthorizationClient authorizationClient(ProcessingProvider provider) {
		AuthorizationClient authClient = authClientProvider.get();
		return authClient;
	}
	
	
	public void sendRequiredPromptResponse(ChannelHandlerContext ctx, PosAuthorization request, ProcessingProvider provider) {
		PosAuthorization response = new PosAuthorization(request);
		response.setResponseFlags(request);
		response.setReauth("Required Fields Missing");
		
		provider.setRequiredPrompts(request, response);
		
		String resp = response.toString();
		//logger.debug("SEND: " + resp);
		ctx.write(resp);
 		// close the channel once the content is fully written
    	ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
	
	
	public void sendErrorResponse(ChannelHandlerContext ctx, PosAuthorization request, String error) {
		PosAuthorization response = new PosAuthorization(request);
		response.setResponseFlags(request);
		response.setDenied(error, null);
		
		String resp = response.toString();
//		logger.debug("SEND: " + resp);
		ctx.write(resp);
 		// close the channel once the content is fully written
    	ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("Error: " + cause.getMessage() + ". Context: " + ctx.toString());
		if (cause instanceof ReadTimeoutException) { 
			ctx.close();
		} else {
			ctx.write(String.valueOf(ASCIIChars.NAK));
			ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}
	

	

}
