package com.db.tradeStore.model;

import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Trade Entity Class
 *
 */
@Entity
@IdClass(value = TradePK.class)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Trade implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	private String tradeId;

	@Id
	private int version;

	private String counterPartyId;

	private String bookId;

	private Date maturityDate;

	private Date createdDate;

	private boolean isExpired;
}