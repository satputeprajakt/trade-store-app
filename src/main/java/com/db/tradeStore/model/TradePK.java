package com.db.tradeStore.model;

import lombok.*;

import java.io.Serializable;

/**
 * Primary Key class for Trade Entity
 *
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TradePK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String tradeId;
	
	private int version;
}