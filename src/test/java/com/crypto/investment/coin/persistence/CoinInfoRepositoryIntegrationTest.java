package com.crypto.investment.coin.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.crypto.investment.coin.model.CoinInfoDto;
import com.crypto.investment.price.persistence.Price;
import com.crypto.investment.price.persistence.PriceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "com.crypto.investment.coin.persistence")
class CoinInfoRepositoryIntegrationTest {

  private final LocalDateTime date1 = LocalDateTime.of(2022, Month.JANUARY, 14, 12,0);
  private final LocalDateTime date2 = LocalDateTime.of(2022, Month.JANUARY, 14, 13,0);
  private final LocalDateTime date3 = LocalDateTime.of(2022, Month.JANUARY, 15, 13,0);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private CoinInfoRepository coinInfoRepository;

  @Autowired
  private PriceRepository priceRepository;

  @PersistenceContext
  private EntityManager entityManager;

  @BeforeEach
  void setUp() {

    Price priceBTC1 = generate("50000.00", date1, "BTC");
    Price priceBTC2 = generate("51000.00", date2, "BTC");
    Price priceBTC3 = generate("52000.00", date3, "BTC");
    Price priceETH1 = generate("3000.00", date1, "ETH");
    Price priceETH2 = generate("3200.00", date2, "ETH");
    Price priceETH3 = generate("3300.00", date3, "ETH");

    priceRepository.save(priceBTC1);
    priceRepository.save(priceBTC2);
    priceRepository.save(priceBTC3);
    priceRepository.save(priceETH1);
    priceRepository.save(priceETH2);
    priceRepository.save(priceETH3);

    entityManager.flush();
    entityManager.clear();
  }

  private Price generate(String priceString, LocalDateTime date, String coin) {
    Price price = new Price();
    price.setPrice(new BigDecimal(priceString));
    price.setPriceDate(date);
    price.setCoin(coin);
    return price;
  }

  @Test
  void testGetCoinInfoByCoin() {
    // Act
    CoinInfoDto coinInfo = coinInfoRepository.getCoinInfoByCoin("BTC");

    // Assert
    assertThat(coinInfo).isNotNull();
    assertThat(coinInfo.max()).isEqualTo(new BigDecimal("52000.00"));
    assertThat(coinInfo.min()).isEqualTo(new BigDecimal("50000.00"));
    assertThat(coinInfo.oldest()).isEqualTo(LocalDateTime.of(2022, Month.JANUARY, 14, 12, 0));
    assertThat(coinInfo.newest()).isEqualTo(LocalDateTime.of(2022, Month.JANUARY, 15, 13, 0));
  }

  @Test
  void testGetCoinListSortedByNormalizedRange() {
    // Act
    List<String> coinList = coinInfoRepository.getCoinListSortedByNormalizedRange();

    // Assert
    assertThat(coinList).isNotNull();
    assertThat(coinList).hasSize(2);
    assertThat(coinList.get(0)).isEqualTo("ETH");
    assertThat(coinList.get(1)).isEqualTo("BTC");
  }

  @Test
  void testGetCoinWithMaxNormalizedRange() {
    // Act
    String coin = coinInfoRepository.getCoinWithMaxNormalizedRange(LocalDate.of(2022, Month.JANUARY, 14));

    // Assert
    assertThat(coin).isEqualTo("ETH");
  }
}
