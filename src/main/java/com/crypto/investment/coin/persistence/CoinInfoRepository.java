package com.crypto.investment.coin.persistence;

import com.crypto.investment.coin.model.CoinInfoDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Provides data access methods for retrieving and manipulating coin information from the database.
 */
@Repository
public class CoinInfoRepository {

  /**
   * SQL query to select the maximum and minimum price, oldest and newest price dates for a
   * specified coin.
   */
  public static final String SELECT_COIN_INFO = "select max(price) as maxPrice, min(price) as minPrice, min(price_date) as oldest, max(price_date) as newest from price where coin = ?";
  /**
   * SQL query to select coins ordered by their normalized range, calculated as (max(price) -
   * min(price)) / min(price).
   */
  public static final String SELECT_COIN_WITH_RANGE = "select coin from price group by coin order by (max(price) - min(price)) / min(price) desc";
  /**
   * SQL query to select the coin with the maximum normalized range for a specified date. The date
   * format is 'dd-MM-yyyy'.
   */
  public static final String SELECT_COIN_WITH_MAX_RANGE = "select coin from price where to_char(price_date,'dd-mm-yyyy')  = ? group by coin order by (max(price) - min(price)) / min(price) desc limit 1;";

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  private final JdbcTemplate jdbcTemplate;


  @Autowired
  public CoinInfoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Retrieves information about a specific coin, including the maximum price, minimum price, oldest
   * price date, and newest price date.
   *
   * @param coin The symbol of the coin to retrieve information for.
   * @return A {@link CoinInfoDto} object containing the coin information.
   */
  public CoinInfoDto getCoinInfoByCoin(String coin) {
    Object[] args = {coin};
    return jdbcTemplate.queryForObject(SELECT_COIN_INFO,
        (rs, rowNum) -> new CoinInfoDto(coin, new BigDecimal(rs.getString("maxPrice")),
            new BigDecimal(rs.getString("minPrice")), rs.getTimestamp("oldest").toLocalDateTime(),
            rs.getTimestamp("newest").toLocalDateTime()), args);
  }

  /**
   * Retrieves a list of all coins sorted by their normalized range in descending order.
   *
   * @return A list of coin symbols ordered by their normalized range.
   */
  public List<String> getCoinListSortedByNormalizedRange() {
    return jdbcTemplate.query(SELECT_COIN_WITH_RANGE, (rs, rowNum) -> rs.getString("coin"));
  }

  /**
   * Retrieves the coin with the maximum normalized range for a specified date.
   *
   * @param dateTime The date for which to find the coin with the maximum normalized range.
   * @return The symbol of the coin with the maximum normalized range.
   */
  public String getCoinWithMaxNormalizedRange(LocalDate dateTime) {
    Object[] args = {dateTime.format(formatter)};
    return jdbcTemplate.queryForObject(SELECT_COIN_WITH_MAX_RANGE,
        (rs, rowNum) -> rs.getString("coin"), args);
  }

}
