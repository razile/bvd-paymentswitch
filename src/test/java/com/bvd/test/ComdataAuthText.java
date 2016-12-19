package com.bvd.test;

public class ComdataAuthText {

	public static void main(String[] args) {
		
		String reply = "CTL#227819    $   592.54 TAX -   68.17";
				
		int indexOfAuth = reply.indexOf("CTL#") + 4;
		int indexOfAmount = reply.indexOf("$") + 1;
		int indexOfTax = reply.indexOf("TAX");
	    
		String authCode = reply.substring(indexOfAuth, indexOfAmount).trim();
		String amount = reply.substring(indexOfAmount, indexOfTax).trim();
		String message = reply.substring(indexOfTax).trim();
		System.out.println("Auth: " + authCode + ", Amount: " + amount + ", Message: " + message);
	}

}
