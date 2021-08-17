package com.db.tradeStore.service;

import java.util.List;

import com.db.tradeStore.model.Trade;

public interface TradeStoreService {
	
	List<Trade> findTrades(String tradeId);
	
	void saveTrade(Trade trade);
	
	void expireTrades();
}