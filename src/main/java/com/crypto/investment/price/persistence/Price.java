package com.crypto.investment.price.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Represents the price of a cryptocurrency.
 */
@Entity
@Table(name = "price")
@Data
public class Price {

  /**
   * The unique identifier for the price entry. This field is the primary key and is
   * auto-generated.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The date and time when the price was recorded.
   */
  @Column(name = "price_date", columnDefinition = "TIMESTAMP")
  private LocalDateTime priceDate;

  /**
   * The price of the cryptocurrency.
   */
  @Column
  private BigDecimal price;

  /**
   * The symbol of the cryptocurrency (e.g., BTC, ETH).
   */
  @Column
  private String coin;
}
