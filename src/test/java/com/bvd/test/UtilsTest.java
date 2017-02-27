package com.bvd.test;

public class UtilsTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String cardToken = "5567356025866229=20104";
		
		int maxIndex = cardToken.length();
		int calcIndex = cardToken.indexOf("=") + 5;
		int endIndex = (calcIndex > maxIndex)?maxIndex:calcIndex;
		
		cardToken = cardToken.substring(0, endIndex);
		
		System.out.println(cardToken);
		
	}

}
