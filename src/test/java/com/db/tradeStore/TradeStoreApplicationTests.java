package com.db.tradeStore;

import com.db.tradeStore.constant.Constants;
import com.db.tradeStore.controller.TradeStoreController;
import com.db.tradeStore.model.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TradeStoreApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    TradeStoreController tradeController;

    @Test
    void testSaveTrade() {
        tradeController.saveTrade(createTrade("T1", 1, "CP-1", "B1", setDate(), false));
        ResponseEntity<List<Trade>> tradeList = tradeController.getTrades();
        Assertions.assertEquals(1, tradeList.getBody().size());
        Assertions.assertEquals("T1", tradeList.getBody().get(0).getTradeId());
    }

    @Test
    void testSaveTradeWhenPastMaturityDate() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 10);
            cal.set(Calendar.HOUR_OF_DAY, 0);
		    cal.set(Calendar.MINUTE, 0);
		    cal.set(Calendar.SECOND, 0);
		    cal.set(Calendar.MILLISECOND, 0);
            tradeController.saveTrade(createTrade("T2", 1, "CP-1", "B1", cal.getTime(), false));
        } catch (Exception e) {
            Assertions.assertEquals(Constants.MATURITY_DT_VALIDATION_MSG,e.getMessage());
        }
    }

    @Test
    void testSaveTradeWhenOldVersion() {
        tradeController.saveTrade(createTrade("T2", 2, "CP-2", "B1", setDate(), false));
        ResponseEntity<List<Trade>> tradeList = tradeController.getTrades();
        Assertions.assertEquals(2, tradeList.getBody().get(0).getVersion());
        try {
            tradeController.saveTrade(createTrade("T2", 1, "CP-1", "B1", setDate(), false));
        } catch (Exception e) {
            Assertions.assertEquals(Constants.VERSION_VALIDATION_MSG,e.getMessage());
        }
    }

    @Test
    void testSaveTradeWhenSameVersion() {
        tradeController.saveTrade(createTrade("T3", 1, "CP-1", "B1", setDate(), false));
        ResponseEntity<List<Trade>> tradeList = tradeController.getTrades();
        Assertions.assertEquals("B1", tradeList.getBody().get(0).getBookId());

        tradeController.saveTrade(createTrade("T3", 1, "CP-1", "B2", setDate(), false));
        ResponseEntity<List<Trade>> tradeList2 = tradeController.getTrades();
        Assertions.assertEquals("B2", tradeList2.getBody().get(0).getBookId());
    }

    private Trade createTrade(String tradeId, int version, String counterPartyId, String bookId, Date maturityDate, boolean isExpired) {
        Trade trade = Trade.builder().tradeId(tradeId).version(version).counterPartyId(counterPartyId).bookId(bookId).maturityDate(maturityDate).isExpired(isExpired).build();
        return trade;
    }
    
    private Date setDate() {
        Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}