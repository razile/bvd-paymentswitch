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
package com.bvd.paymentswitch.processing.client;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;
import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Qualifier("authorizationHandler")
@Scope("prototype")
public class AuthorizationHandler extends SimpleChannelInboundHandler<String>  {

	static final Logger logger = LoggerFactory.getLogger(AuthorizationHandler.class);
	static final Logger responseLogger = LoggerFactory.getLogger("Response-Logger");
	
	protected PosAuthorization posRequest;
	protected ProcessorAuthorization processorRequest;
	protected ProcessingProvider processingProvider;
	protected CompletableFuture<String> authorizationFuture;
	
	
	public void initializePOSContext(PosAuthorization posRequest, ProcessorAuthorization processorRequest, ProcessingProvider processingProvider) {
		this.posRequest = posRequest;
		this.processingProvider = processingProvider;
		this.processorRequest = processorRequest;
	}
	
	public void setAuthorizationFuture(CompletableFuture<String> authFuture) {
		this.authorizationFuture = authFuture;
	}
	

	@Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
    	
    	logger.debug("READ: " + msg);
    	
    	responseLogger.info(msg);
    	ProcessorAuthorization processorResponse = processingProvider.parseProcessorResponse(posRequest, msg); 	 	
    	
    	PosAuthorization posResponse = processingProvider.createPosResponse(posRequest, processorResponse);
    	
    	String bvdResp = posResponse.toString();
    	
    	processingProvider.saveAuthorization(posRequest, processorResponse);
    	
    	authorizationFuture.complete(bvdResp);
    	
    	if (processingProvider.getPaymentProcessor().isClientDisconnect()) {
    		ctx.close();
    	}
    }

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.toString());
        
        if (cause instanceof ReadTimeoutException) {
        	logger.debug("Processor timed out");
        	
        	if (posRequest.getTransactionType() == 1)  {
        		// complete the transaction 
        		processorRequest.setType("RC");
        		processorRequest.setResponseCode("F");
            	PosAuthorization posResponse = processingProvider.createPosResponse(posRequest, processorRequest);      
            	String bvdResp = posResponse.toString();     
        		processingProvider.saveAuthorization(posRequest, processorRequest);
            	authorizationFuture.complete(bvdResp);
        	} else {
        		authorizationFuture.complete(String.valueOf(ASCIIChars.NAK));
        	}
	    	ctx.close();
        } else {
        	authorizationFuture.complete(String.valueOf(ASCIIChars.NAK));
        	ctx.close();
        }
    }
    

}
