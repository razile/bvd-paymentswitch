package com.bvd.test;

import java.math.BigDecimal;

import com.bvd.paymentswitch.utils.ASCIIChars;
import com.bvd.paymentswitch.utils.ProtocolUtils;

public class ComdataAuthText {

	public static void main(String[] args) {
		
		String reply = "CTL#227819    $   592.54 TAX -   68.17";
	
		int indexOfAuth = reply.indexOf("CTL#");
		int indexOfAmount = reply.indexOf("$");
		int indexOfTax = reply.indexOf("TAX");
		
		String authCode = null; 
		String amount = null; 
		String message = null;
		
		try {
			authCode = reply.substring(indexOfAuth + 4, indexOfAuth + 10).trim();
		} catch (Exception e) {
			authCode = "000000";
		}
		
		try {
			amount = reply.substring(indexOfAmount + 1, indexOfAmount + 10).trim();
		} catch (Exception e) {
			amount = "0.00";
		}
		
		try {
			message = reply.substring(indexOfTax, indexOfTax + 13).trim();
		} catch (Exception e) {
			message = null;
		}
	    
		//String authCode = reply.substring(indexOfAuth, indexOfAmount).trim();
		//String amount = reply.substring(indexOfAmount, indexOfTax).trim();
		//String message = reply.substring(indexOfTax).trim();
		System.out.println("Auth: " + authCode + ", Amount: " + amount + ", Message: " + message + "END");
		
		
		reply = "088250700999990882507013333900012000882507009999900000000000000000000000000000000000000000000000002000105308825070099999";
		
		String dollarLimit = null;
		int code = 0;
		if (code == 1) {
			dollarLimit = reply.substring(49,56).trim();
		} else if (code  == 2){
			dollarLimit = reply.substring(35,42).trim();
		} else {
			dollarLimit = reply.substring(0,7).trim(); 
		}
		
		dollarLimit = dollarLimit.substring(0, dollarLimit.length()-2) + "." + dollarLimit.substring(dollarLimit.length()-2); 
		
		BigDecimal dollars = ProtocolUtils.getBigDecimal(dollarLimit,2);
		
		System.out.println("DollarLimit: " + dollarLimit + " Amount: " + dollars);
		
		
		String request = "ON305TTCPISP00014/00085/A5600171620203812=49121201491/1234567890/123456/3812/123456/150000/2000/" + null + "/" + null + "/ON/" + null + "/P/053";
		System.out.println(request);
		request = request.replace("null","");
		System.out.print(request);
	}

}
