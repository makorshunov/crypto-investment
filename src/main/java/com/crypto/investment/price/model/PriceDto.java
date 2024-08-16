package com.crypto.investment.price.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents the price information of a cryptocurrency.
 *
 * @param dateTime The price date and time.
 * @param coin     The symbol of the cryptocurrency (e.g., BTC, ETH).
 * @param price    The price of the cryptocurrency at the specified {@code dateTime}.
 */
public record PriceDto(LocalDateTime dateTime, String coin, BigDecimal price) {

}
