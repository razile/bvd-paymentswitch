package com.bvd.paymentswitch.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.utils.ProtocolUtils;

@Controller
@RequestMapping("/authorizations/pos")
public class PosAuthorizationsController {

	@Autowired
	private AuthorizationService authService;

	
	@RequestMapping(method=RequestMethod.GET, value= "/{authId}")
	public @ResponseBody String getFuelCode(
										@PathVariable String authId,
										@RequestParam(value="transTS") String transTS
										) 
	
	{ 
		String resp = authService.getFuelCodeForAuthorization(authId, ProtocolUtils.stringToTimestamp(transTS));
		return resp;
	}
	

}
