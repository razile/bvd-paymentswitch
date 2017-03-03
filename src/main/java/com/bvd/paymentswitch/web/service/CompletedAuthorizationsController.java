package com.bvd.paymentswitch.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.CompletedAuthorization;
import com.bvd.paymentswitch.utils.ProtocolUtils;

@Controller
@RequestMapping("/transactions/fuelcard")
public class CompletedAuthorizationsController {

	@Autowired
	private AuthorizationService authService;
	
	
	@RequestMapping(method = RequestMethod.GET) 
	public @ResponseBody List<CompletedAuthorization> getTransactions(
										@RequestParam(value="startTS", required=false) String startTS,
										@RequestParam(value="endTS", required=false) String endTS,
										@RequestParam(value="type", defaultValue="transaction") String type
										) 
	
	{
		return authService.getCompletedAuthorizations(ProtocolUtils.stringToTimestamp(startTS),ProtocolUtils.stringToTimestamp(endTS),type);
	}
	

}
