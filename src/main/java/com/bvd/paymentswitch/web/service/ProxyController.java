package com.bvd.paymentswitch.web.service;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


@Controller
@RequestMapping("/paymentProxy")
public class ProxyController {

	
	@RequestMapping(value="/{resId}")
	@ResponseBody
	public String mirrorRest(@RequestBody String body, HttpMethod method, @PathVariable String resId, HttpServletRequest request,
	    HttpServletResponse response) throws URISyntaxException
	{
	
		String tdpUri = new String("https://restpayqa.jetblue.com/tdprest-2/api/reservations/{resId}/fulfill"); 
		
		Map<String, String> vars = new HashMap<String, String>();
	    vars.put("resId", resId);
		// String host = "restpayqa.jetblue.com";
	    // String path = "tdprest-2/api/reservations/{resId}/fulfill";
	    // URI uri = new URI("https",host,path,null);
	    
	    // "https", null, "restpayqa.jetblue.com", 443, request.getRequestURI(), request.getQueryString(), null);

	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	    
	  	headers.add("accept-encoding", request.getHeader("Accept-Encoding"));
	    headers.add("user-agent", "jetBlue/1822 CFNetwork/758.2.8 Darwin/15.0.0");
	    headers.add("apiKey", request.getHeader("apiKey"));
	    headers.add("authorization", request.getHeader("Authorization"));    
	    headers.add("content-type", "application/json");
	    headers.add("content-length", request.getHeader("Content-Length"));
	    headers.add("host", "www.jetblue.com");
	    headers.add("connection", request.getHeader("Connection"));
	    headers.add("accept", "application/json");
	    
	    HttpEntity<String> dsreq = new HttpEntity<String>(body, headers);
	    
	    RestTemplate rt = new RestTemplate();
	    ResponseEntity<String> responseEntity =
	        rt.exchange(tdpUri, method, dsreq, String.class, vars);

	    return responseEntity.getBody();
	}
	
}
