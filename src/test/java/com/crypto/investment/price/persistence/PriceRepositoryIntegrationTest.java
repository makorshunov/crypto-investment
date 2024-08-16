package com.crypto.investment.price.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class PriceRepositoryIntegrationTest {

  @Autowired
  private PriceRepository priceRepository;

  @PersistenceContext
  private EntityManager entityManager;

  @BeforeEach
  void setUp() {
    Price price1 = new Price();
    price1.setPrice(new BigDecimal("50000.00"));
    price1.setPriceDate(LocalDateTime.now().minusDays(1));
    price1.setCoin("BTC");

    Price price2 = new Price();
    price2.setPrice(new BigDecimal("2000.00"));
    price2.setPriceDate(LocalDateTime.now());
    price2.setCoin("ETH");

    priceRepository.save(price1);
    priceRepository.save(price2);

    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @Transactional
  void testFindAll() {
    List<Price> prices = priceRepository.findAll();

    assertThat(prices).hasSize(2);
    assertThat(prices.get(0).getCoin()).isEqualTo("BTC");
    assertThat(prices.get(0).getPrice()).isEqualTo(new BigDecimal("50000.00"));
    assertThat(prices.get(1).getCoin()).isEqualTo("ETH");
    assertThat(prices.get(1).getPrice()).isEqualTo(new BigDecimal("2000.00"));
  }

  @Test
  @Transactional
  void testSave() {
    Price newPrice = new Price();
    newPrice.setPrice(new BigDecimal("1000"));
    newPrice.setPriceDate(LocalDateTime.now());
    newPrice.setCoin("LTC");

    Price savedPrice = priceRepository.save(newPrice);

    assertThat(savedPrice.getId()).isNotNull();
    assertThat(savedPrice.getCoin()).isEqualTo("LTC");
    assertThat(savedPrice.getPrice()).isEqualTo(new BigDecimal("1000"));
  }
}