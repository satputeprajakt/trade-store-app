package com.db.tradeStore.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.db.tradeStore.service.TradeStoreService;

@Component
public class TradeExpiryScheduler {
	
	@Autowired
	TradeStoreService tradeStoreService;
	
	@Scheduled(cron="{trade.expiry.job.schedule}")
	public void expireTrades(){
		tradeStoreService.expireTrades();
	}
}