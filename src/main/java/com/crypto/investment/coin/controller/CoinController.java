package com.crypto.investment.coin.controller;

import com.crypto.investment.coin.model.CoinInfoDto;
import com.crypto.investment.coin.persistence.CoinInfoRepository;
import com.crypto.investment.coin.persistence.CoinRepository;
import com.crypto.investment.exception.NotSupportedCoinException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring REST controller that handles requests related to cryptocurrency data.
 */
@RestController
public class CoinController implements CoinApi {

  /**
   * The {@link CoinInfoRepository} used to access coin information from the database.
   */
  private final CoinInfoRepository coinInfoRepository;

  /**
   * The {@link CoinRepository} used to verify if a coin is supported.
   */
  private final CoinRepository coinRepository;

  @Autowired
  public CoinController(CoinInfoRepository coinInfoRepository, CoinRepository coinRepository) {
    this.coinInfoRepository = coinInfoRepository;
    this.coinRepository = coinRepository;
  }

  /**
   * Retrieves detailed information about a specific coin. If the coin is not supported, a
   * {@link NotSupportedCoinException} is thrown.
   *
   * @param coin The symbol of the coin for which information is requested.
   * @return A {@link CoinInfoDto} object containing the coin's information.
   * @throws NotSupportedCoinException If the coin is not supported.
   */
  @Override
  public CoinInfoDto getCoinInfo(String coin) {
    validateCoin(coin);
    return coinInfoRepository.getCoinInfoByCoin(coin);
  }


  /**
   * Retrieves a list of all supported coins, sorted by their normalized range in descending order.
   *
   * @return A list of coin symbols ordered by their normalized range.
   */
  @Override
  public List<String> getCoinListNormalizedRange() {
    return coinInfoRepository.getCoinListSortedByNormalizedRange();
  }

  /**
   * Retrieves the coin with the maximum normalized range for a specified date.
   *
   * @param date The date for which to find the coin with the maximum normalized range.
   * @return The symbol of the coin with the maximum normalized range for the given date.
   */
  @Override
  public String getCoinWithMaxNormalizedRange(LocalDate date) {
    return coinInfoRepository.getCoinWithMaxNormalizedRange(date);
  }

  /**
   * Validates whether a given coin symbol is supported by the system.
   *
   * @param coin The symbol of the coin to validate.
   * @throws NotSupportedCoinException If the coin is not supported.
   */
  private void validateCoin(String coin) {
    if (!coinRepository.containsCoin(coin)) {
      throw new NotSupportedCoinException("This coin: " + coin + " doesn't supported!");
    }
  }
}
