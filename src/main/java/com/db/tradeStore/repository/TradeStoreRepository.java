package com.db.tradeStore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.db.tradeStore.model.Trade;
import com.db.tradeStore.model.TradePK;

public interface TradeStoreRepository extends JpaRepository<Trade, TradePK> {
	
	/**
	 * Find Trade by Id
	 *@param tradeId
	 *@return List
	 */
	List<Trade> findByTradeId(String tradeId);
}