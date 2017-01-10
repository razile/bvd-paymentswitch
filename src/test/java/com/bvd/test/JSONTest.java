package com.bvd.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bvd.paymentswitch.web.service.models.fuelcard.*;
import com.bvd.paymentswitch.web.service.models.fuelcard.Request;
import com.bvd.paymentswitch.web.service.models.reject.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Request reqFC = new Request(); 
		reqFC.setAccountNumber("111222");
		reqFC.setCardGroup("1");
		reqFC.setCompanyNumber("233");
		reqFC.setReferenceAuthNumber("12344");
		reqFC.setStartDate("20170108");
		reqFC.setTransactionID("33333");
		
		ObjectMapper mapper = new ObjectMapper();
		String result = mapper.writeValueAsString(reqFC);
		System.out.println(result);
		
		//{"companyNumber":"233","accountNumber":"111222","cardGroup":"1","referenceAuthNumber":"12344","startDate":"20170108"};
		
		Request reqfc2 = mapper.readValue(result, Request.class);
		reqfc2.setTransactionID("44444");
		
		List<Request> trans = new ArrayList<Request>();
		trans.add(reqFC);
		trans.add(reqfc2);
		
		
		String listresult = mapper.writeValueAsString(trans);
		System.out.println(listresult);
		
	}
	

}
