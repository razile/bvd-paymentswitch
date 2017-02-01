package com.bvd.paymentswitch.web.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.jb.proxy.request.PaymentRequest;
import com.jb.proxy.response.PaymentResponse;

@Controller
@RequestMapping("/paymentProxy")
public class ProxyController {

	@RequestMapping(method = RequestMethod.POST, value="/{resId}")
	public @ResponseBody PaymentResponse proxyRequest(@RequestBody PaymentRequest request, @PathVariable String resId, HttpServletRequest context) {
		
		String tdpUri = new String("https://restpayqa.jetblue.com/tdprest-2/api/reservations/{resId}/fulfill"); 
		
		Map<String, String> vars = new HashMap<String, String>();
	    vars.put("resId", resId);
	    
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	    
	  

	    headers.add("Accept-Encoding", context.getHeader("Accept-Encoding"));
	    headers.add("User-Agent", "jetBlue/1822 CFNetwork/758.2.8 Darwin/15.0.0");
	    headers.add("apiKey", context.getHeader("apiKey"));
	    headers.add("Authorization", context.getHeader("Authorization"));    
	    headers.add("Content-Type", "application/json");
	    headers.add("Content-Length", context.getHeader("Content-Length"));
	    headers.add("Host", context.getHeader("Host"));
	    headers.add("Connection", context.getHeader("Connection"));

	    

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

	    HttpEntity<PaymentRequest> dsreq = new HttpEntity<PaymentRequest>(request, headers);

	    try {
	    	PaymentResponse response = restTemplate.postForObject(tdpUri, dsreq, PaymentResponse.class, vars);
	    	return response;
	    } catch (Exception e) {
	    	e.printStackTrace();	   
	    }
	    return new PaymentResponse();
	}
	
}
