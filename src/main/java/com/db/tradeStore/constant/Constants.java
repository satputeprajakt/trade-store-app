package com.db.tradeStore.constant;

public interface Constants {
	
	String TRADES = "/trades";
	
	String MATURITY_DT_VALIDATION_MSG = "Incoming Trade has been rejected since its maturity date is less than today's date.";
	
	String VERSION_VALIDATION_MSG = "Incoming Trade has been rejected since a higher version of the Trade is already present";
	
	String TECHNICAL_EXCEPTION = "Technical Exception while validating the trade version";
}