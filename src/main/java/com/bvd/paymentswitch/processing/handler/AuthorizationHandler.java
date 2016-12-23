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
package com.bvd.paymentswitch.processing.handler;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;
import com.bvd.paymentswitch.utils.ASCIIChars;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AuthorizationHandler extends SimpleChannelInboundHandler<String>  {

	static final Logger logger = LoggerFactory.getLogger(AuthorizationHandler.class);
	static final Logger responseLogger = LoggerFactory.getLogger("Response-Logger");
	
	protected ChannelHandlerContext posCtx;
	protected PosAuthorization posRequest;
	protected ProcessingProvider processingProvider;
	

	
	public void initializePOSContext(PosAuthorization posRequest, ProcessingProvider processingProvider, ChannelHandlerContext posCtx) {
		this.posCtx = posCtx;
		this.posRequest = posRequest;
		this.processingProvider = processingProvider;
	}
	

	public abstract void handleResponse(ProcessorAuthorization processorResponse);
	public abstract String formatPosPrompt(String processorValue);
	
	

	@Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
    	
    	logger.debug("READ: " + msg);
    	
    	responseLogger.info(msg);
    	ProcessorAuthorization processorResponse = processingProvider.parseProcessorResponse(posRequest, msg); 	
    	
    	processingProvider.saveProcessorAuthorization(processorResponse);
    	
    	handleResponse(processorResponse);
    	
    	if (processingProvider.getPaymentProcessor().isClientDisconnect()) {
    		ctx.close();
    	}
    }

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.getMessage());
        posCtx.write(String.valueOf(ASCIIChars.NAK));
		posCtx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        ctx.close();
    }
}
