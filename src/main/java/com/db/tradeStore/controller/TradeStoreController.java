package com.db.tradeStore.controller;

import com.db.tradeStore.constant.Constants;
import com.db.tradeStore.model.Trade;
import com.db.tradeStore.service.TradeStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class TradeStoreController {

    @Autowired
    TradeStoreService tradeStoreService;

    /**
     * API to fetch all the trades
     *
     * @return List
     */
    @GetMapping(value = Constants.TRADES, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Trade>> getTrades() {
        return new ResponseEntity<>(tradeStoreService.findTrades(null), HttpStatus.OK);
    }

    /**
     * API to fetch the trade by Id
     *
     * @param tradeId
     * @return List
     */
    @GetMapping(value = Constants.TRADES + "/{tradeId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Trade>> getTradeById(@PathVariable("tradeId") String tradeId) {
        return new ResponseEntity<>(tradeStoreService.findTrades(tradeId), HttpStatus.OK);
    }

    /**
     * API to save the trade
     *s
     * @param trade
     * @return status
     */
    @PostMapping(value = Constants.TRADES, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveTrade(@RequestBody Trade trade) {
        tradeStoreService.saveTrade(trade);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}