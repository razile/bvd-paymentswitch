package com.bvd.paymentswitch.models;

public enum CustomPrompts {
	HBRD	("P1", "Enter Hubometer Reading"),
	TRIP	("P2","Enter Trip Number"),
	HRRD	("P3", "Enter Reefer Hour Meter Reading");
	
	private String element;
	private String prompt;
	
	CustomPrompts(String element, String prompt) {
		this.setElement(element);
		this.setPrompt(prompt);
	}


	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}


	public String getElement() {
		return element;
	}


	public void setElement(String element) {
		this.element = element;
	}
	
	
	
	
}
