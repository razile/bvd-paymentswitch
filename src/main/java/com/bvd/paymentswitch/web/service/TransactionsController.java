package com.bvd.paymentswitch.web.service;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/transactions")
public class TransactionsController {

	@RequestMapping(method = RequestMethod.GET) 
	public @ResponseBody List<ProcessorAuthorization> getTransactions() {
		
		
	}
}
