package com.crypto.investment.coin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CoinInfoDto(
    @Schema(name = "coin", description = "Coin short name", example = "BTC") String coin,
    @Schema(name = "max", description = "Coin maximum value", example = "55000.00") BigDecimal max,
    @Schema(name = "min", description = "Coin minimum value", example = "20000.00") BigDecimal min,
    @Schema(name = "oldest", description = "Coin oldest value", example = "2022-01-01T04:00:00") LocalDateTime oldest,
    @Schema(name = "newest", description = "Coin newest value", example = "2022-31-01T04:00:00") LocalDateTime newest) {

}
