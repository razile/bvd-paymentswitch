package com.bvd.paymentswitch.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.RejectedAuthorization;
import com.bvd.paymentswitch.utils.ProtocolUtils;;

@Controller
@RequestMapping("/transactions/rejected")
public class RejectedAuthorizationsController {

	@Autowired
	private AuthorizationService authService;

	
	@RequestMapping(method = RequestMethod.GET) 
	public @ResponseBody Iterable<RejectedAuthorization> getTransactions(
										@RequestParam(value="startTS",required=false) String startTS,
										@RequestParam(value="endTS", required=false) String endTS
										) 
	
	{
	
		return authService.getRejectedAuthorizations(ProtocolUtils.stringToTimestamp(startTS),ProtocolUtils.stringToTimestamp(endTS));
	}
}
