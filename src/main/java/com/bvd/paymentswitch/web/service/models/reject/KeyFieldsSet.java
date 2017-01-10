package com.bvd.paymentswitch.web.service.models.reject;

public class KeyFieldsSet {
	
	private LastReceivedFieldsSet lastReceivedFieldsSet;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String numberOfTransactionsRequested;


	public LastReceivedFieldsSet getLastReceivedFieldsSet() {
		return lastReceivedFieldsSet;
	}

	public void setLastReceivedFieldsSet(LastReceivedFieldsSet lastReceivedFieldsSet) {
		this.lastReceivedFieldsSet = lastReceivedFieldsSet;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getNumberOfTransactionsRequested() {
		return numberOfTransactionsRequested;
	}

	public void setNumberOfTransactionsRequested(String numberOfTransactionsRequested) {
		this.numberOfTransactionsRequested = numberOfTransactionsRequested;
	}
	
	
}
