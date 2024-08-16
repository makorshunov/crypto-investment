package com.crypto.investment.coin.controller;

import com.crypto.investment.coin.model.CoinInfoDto;
import com.crypto.investment.exception.ExceptionInformation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/v1/coin")
@Tag(name = "Coin API", description = "Crypto recommendation api interface")
public interface CoinApi {

  @Operation(summary = "Get Coin financial information", tags = {
      "Coin API"}, description = "Get Coin financial information.  Calculates oldest/newest/min/max value for coin.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Coin financial information successfully returned", content = @Content(schema = @Schema(implementation = CoinInfoDto.class))),
      @ApiResponse(responseCode = "400", description = "Unsupported coin parameter", content = @Content(schema = @Schema(implementation = ExceptionInformation.class))),
      @ApiResponse(responseCode = "500", description = "General application error", content = @Content(schema = @Schema(implementation = ExceptionInformation.class)))})
  @GetMapping("/info/{coin}")
  CoinInfoDto getCoinInfo(
      @PathVariable(name = "coin") @Parameter(name = "coin", description = "Coin name", example = "BTC") String coin);

  @Operation(summary = "Get list of Coin sorted by normalized range.", tags = {
      "Coin API"}, description = "Get list of Coin sorted by normalized range.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of Coins successfully returned"),
      @ApiResponse(responseCode = "500", description = "General application error", content = @Content(schema = @Schema(implementation = ExceptionInformation.class)))})
  @GetMapping("/range")
  List<String> getCoinListNormalizedRange();

  @Operation(summary = "Get Coin with the highest normalized range.", tags = {
      "Coin API"}, description = "Get Coin with the highest normalized range for a specific day.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Coin successfully returned"),
      @ApiResponse(responseCode = "500", description = "General application error", content = @Content(schema = @Schema(implementation = ExceptionInformation.class)))})
  @GetMapping("/max/{date}")
  String getCoinWithMaxNormalizedRange(
      @PathVariable(name = "date") @DateTimeFormat(pattern = "dd-MM-yyyy") @Parameter(name = "date", description = "The day for which the calculation is made", example = "01-01-2022") LocalDate date);

}
