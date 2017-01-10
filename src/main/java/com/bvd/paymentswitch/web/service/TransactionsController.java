package com.bvd.paymentswitch.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.web.service.models.fuelcard.Request;
import com.bvd.paymentswitch.web.service.models.fuelcard.CompletedAuthorization;

@Controller
@RequestMapping("/transactions/fuelcard")
public class TransactionsController {

	@Autowired
	private AuthorizationService authService;
	
	@RequestMapping(method = RequestMethod.GET) 
	public @ResponseBody Iterable<CompletedAuthorization> getTransactions() {
		
		return authService.getCompletedAuthorizations();
	}
}
