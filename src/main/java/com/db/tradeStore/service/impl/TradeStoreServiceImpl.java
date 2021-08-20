package com.db.tradeStore.service.impl;

import com.db.tradeStore.constant.Constants;
import com.db.tradeStore.exception.APIException;
import com.db.tradeStore.model.Trade;
import com.db.tradeStore.repository.TradeStoreRepository;
import com.db.tradeStore.service.TradeStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class TradeStoreServiceImpl implements  TradeStoreService {
	
	@Autowired
	TradeStoreRepository tradeStoreRepo;
	
	/**
	 * Method to fetch Trades
	 *@param tradeId
	 *@return List
	 */
	@Override
	public List<Trade> findTrades(String tradeId) {
		if(StringUtils.hasText(tradeId)) 
			//If tradeId is present	
			return tradeStoreRepo.findByTradeId(tradeId);
		else
			// Return all trades
			return tradeStoreRepo.findAll();
	}
	
	/**
	 * Save the incoming Trade
	 *@param trade
	 */
	@Override
	public void saveTrade(Trade trade) {
		if (validateTradeVersion(trade)) {
			if (validateMaturityDate(trade.getMaturityDate())) {
				tradeStoreRepo.save(trade);
			} else {
				throw new APIException (Constants.MATURITY_DT_VALIDATION_MSG);
			}
		} else {
			throw new APIException (Constants.VERSION_VALIDATION_MSG);
		}
	}
	
	/**
	 * Validate Trade Version
	 *@param trade
	 *@return boolean
	 */
	private boolean validateTradeVersion(Trade trade) {
		boolean isVersionValid = false;
		
		try {
			// Get a list of all trades for the tradeId
			List<Trade> existingTrade = tradeStoreRepo.findByTradeId(trade.getTradeId());
			
			if (!CollectionUtils.isEmpty(existingTrade)) {
				// Only 1 trade is present for the trade id
				if (existingTrade.size() == 1) {
					if (existingTrade.get(0).getVersion() <= trade.getVersion())
						// Trade version greater than existing trade version,
						// Can Insert
						isVersionValid = true;
				} else {
					// More than 1 trade with different versions present for the same Id
					// Get max version for the existing trades
					int maxExistingTradeVersion = existingTrade.stream().max(Comparator.comparing(Trade::getVersion))
							.orElseThrow(NoSuchElementException::new).getVersion();
					
					if (maxExistingTradeVersion <= trade.getVersion())
						// Max trade version greater than existing trade version, can insert
						isVersionValid = true;
				}
			} else {
				// Trade not present, can insert
				isVersionValid = true;
			}				
		} catch (Exception e) {
			throw new APIException(Constants.TECHNICAL_EXCEPTION, e);
		}
		return isVersionValid;
	}

	/**
	 * Validate Trade maturity date
	 *@param maturityDate
	 *@return boolean
	 */
	private boolean validateMaturityDate(Date maturityDate) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return maturityDate.before(cal.getTime()) ? false : true;
	}
	
	/**
	 * Setting the Trades as Expired
	 *
	 */
	@Override
	public void expireTrades() {
		try {
			// Fetch all trades that are not expired and validate maturity date
			List<Trade> allTradeList = tradeStoreRepo.findAll();
			
			if (!CollectionUtils.isEmpty(allTradeList)) {
				allTradeList.stream().filter(t -> !t.isExpired()).forEach(t -> {
					if (!validateMaturityDate(t.getMaturityDate())) {
						// If the trade Maturity Date is less than today's date, set it to expired
						t.setExpired(true);
						tradeStoreRepo.save(t);
					}
				});
			}
		} catch (Exception e) {
			throw new APIException(Constants.TECHNICAL_EXCEPTION, e);
		}
	}
}
