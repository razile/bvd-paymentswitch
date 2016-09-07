/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.bvd.paymentswitch.authorization.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.KardallHostAuthorization;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.PriorPostAuthorization;
import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Qualifier("authorizationHandler")
@Scope("prototype")
public class AuthorizationHandler extends SimpleChannelInboundHandler<String>  {

	static final Logger logger = LoggerFactory.getLogger(AuthorizationHandler.class);
	static final Logger responseLogger = LoggerFactory.getLogger("Response-Logger");
	
	private ChannelHandlerContext bvdCtx;
	private KardallHostAuthorization request;
	private PaymentProcessor paymentProcessor;
	

	@Autowired
	private AuthorizationService authService;

	
	public ChannelHandlerContext getBvdCtx() {
		return bvdCtx;
	}


	public void setBvdCtx(ChannelHandlerContext bvdCtx) {
		this.bvdCtx = bvdCtx;
	}
	
	public KardallHostAuthorization getRequest() {
		return request;
	}

	public void setRequest(KardallHostAuthorization request) {
		this.request = request;
	}
	
	

    public PaymentProcessor getPaymentProcessor() {
		return paymentProcessor;
	}


	public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
		this.paymentProcessor = paymentProcessor;
	}


	@Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
    	
    	logger.debug("READ: " + msg);
    	
    	
    	responseLogger.info(msg);
    	PriorPostAuthorization auth = new PriorPostAuthorization(request.getId(), msg);
    	auth = authService.saveAuthorization(auth);
    	
    	KardallHostAuthorization response = new KardallHostAuthorization(request);
    	
    	String type = auth.getType();
    	response.setHasStandardPrompts(0);
    	response.setHasCustomPrompts(0);
    	response.setTrainingExclusionTimes(0);
		
		if (type.equals("PC")) {
			// this is a pre-auth approval
			response.setAuthId(auth.getAuthorizationCode());
			response.setAuthorization(1);
			response.setSource(1);
			response.setPinFlag(1);
			response.setReAuthorizationFlag(0);
			response.setTransactionType(0);
			
			if (auth.getFuel() != null) {
				// set limits based on FUEL
				String[] fuel = auth.getFuel().split(",");
				String litres = fuel[0];
				String dollars = fuel[1];
				if (litres.equals("0")) {
					response.setDollarLimit(ProtocolUtils.getBigDecimal(dollars,2));
				} else {
					response.setVolumeLimit(ProtocolUtils.getBigDecimal(litres,3));
				}
				
			} else if (auth.getFuelLimit() != null) {
				// set limits based on FLMT
				String[] flmt = auth.getFuelLimit().split("/");
				
				// need to check this against fuel types
				String[] fuel = flmt[0].split(",");
				String litres = fuel[1];
				String dollars = fuel[2];
				if (litres.equals("0")) {
					response.setDollarLimit(ProtocolUtils.getBigDecimal(dollars,2));
				} else {
					response.setVolumeLimit(ProtocolUtils.getBigDecimal(litres,3));
				}
				
			} else {
				response.setDollarLimit(auth.getTotal());
			}
			ProtocolUtils.setPrompts(response, auth);
			ProtocolUtils.setCustomPrompts(response, auth);
			
		} else if (type.equals("XC")) {
			// this is an extended prompt request (more info needed)
			response.setAuthorization(0);
			response.setSource(1);
			response.setPinFlag(1);
			response.setReAuthorizationFlag(1);
			response.setTransactionType(0);
			
			response.setDenialReason(auth.getErrorCode());
			response.setMessage(auth.getMessage());
			ProtocolUtils.setPrompts(response, auth);
			ProtocolUtils.setCustomPrompts(response, auth);
			
    	} else if (type.equals("RC")) {
			// this is a post-auth approval
    		response.setAuthorization(1);
    		response.setSource(1);
    		response.setPinFlag(1);
    		response.setReAuthorizationFlag(0);
    		response.setTransactionType(1);
			
    		response.setAmount(auth.getTotal());
			
		} else {
			// this is a denial
			response.setAuthorization(0);
			response.setSource(1);
			response.setPinFlag(0);
			response.setReAuthorizationFlag(1);
			response.setTransactionType(0);
			response.setDenialReason(auth.getErrorCode());
		}
    	
		
    	if (auth.getPrint() != null) {
    		response.setReceiptTrailer(auth.getPrint());
    		response.setReceiptTrailerFlag(1);
    	} else {
    		response.setReceiptTrailerFlag(0);
    	}
    	
    	response.setMessage(auth.getMessage());
    	String bvdResp = response.toString();
    	logger.debug("SEND: " + bvdResp);
    	bvdCtx.write(bvdResp);

 		// close the channel once the content is fully written
    	bvdCtx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    	
    	if (paymentProcessor.isClientDisconnect()) {
    		ctx.close();
    	}
    }


    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.getMessage());
        bvdCtx.write(String.valueOf(ASCIIChars.NAK));
		bvdCtx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        ctx.close();
    }
}
