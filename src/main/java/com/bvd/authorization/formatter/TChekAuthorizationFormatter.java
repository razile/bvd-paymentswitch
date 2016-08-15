package com.bvd.authorization.formatter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.bvd.authorization.client.AuthorizationHandler;
import com.bvd.models.KardallHostAuthorization;
import com.bvd.models.PriorAuthRequest;
import com.bvd.models.PriorAuthResponse;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;

public class TChekAuthorizationFormatter extends AbstractAuthorizationFormatter {


	@Override
	public String formatRequest(KardallHostAuthorization request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KardallHostAuthorization formatResponse(String response) {
		// TODO Auto-generated method stub
		return null;
	}



}
