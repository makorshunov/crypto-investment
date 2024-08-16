package com.crypto.investment.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.crypto.investment.coin.persistence.CoinRepository;
import com.crypto.investment.price.model.PriceDto;
import com.crypto.investment.price.persistence.Price;
import com.crypto.investment.price.persistence.PriceRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

class CSVLoaderTest {

  @Mock
  private PriceRepository priceRepository;

  @Mock
  private CoinRepository coinRepository;

  @Mock
  private ResourcePatternResolver resourcePatternResolver;

  @InjectMocks
  private CSVLoader csvLoader;

  @Mock
  private Resource mockResource;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testLoadAllCSV() throws IOException {
    // Mock CSV content
    String csvContent = "timestamp,symbol,price\n" +
        "1642176000000,BTC,40000.50\n" +
        "1642176000000,ETH,2500.00\n";
    when(mockResource.exists()).thenReturn(true);
    when(mockResource.isReadable()).thenReturn(true);
    when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));

    when(resourcePatternResolver.getResources("classpath:prices/*.csv"))
        .thenReturn(new Resource[]{mockResource});

    List<PriceDto> priceDtoList = csvLoader.loadAllCSV();

    assertThat(priceDtoList).isNotNull();
    assertThat(priceDtoList.size()).isEqualTo(2);

    PriceDto firstPrice = priceDtoList.getFirst();
    assertThat(firstPrice.dateTime()).isEqualTo(LocalDateTime.of(2022, Month.JANUARY, 14, 16, 0));
    assertThat(firstPrice.coin()).isEqualTo("BTC");
    assertThat(firstPrice.price()).isEqualTo(new BigDecimal("40000.50"));

    PriceDto secondPrice = priceDtoList.get(1);
    assertThat(secondPrice.coin()).isEqualTo("ETH");
    assertThat(secondPrice.price()).isEqualTo(new BigDecimal("2500.00"));
  }

  @Test
  void testOnApplicationEvent() throws IOException {
    // Mock CSV content
    String csvContent = "timestamp,symbol,price\n" +
        "1642176000000,BTC,40000.50\n";
    when(mockResource.exists()).thenReturn(true);
    when(mockResource.isReadable()).thenReturn(true);
    when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));

    when(resourcePatternResolver.getResources("classpath:prices/*.csv"))
        .thenReturn(new Resource[]{mockResource});

    when(coinRepository.containsCoin("BTC")).thenReturn(false);

    ContextRefreshedEvent event = mock(ContextRefreshedEvent.class);
    csvLoader.onApplicationEvent(event);

    ArgumentCaptor<Price> priceCaptor = ArgumentCaptor.forClass(Price.class);
    verify(priceRepository, times(1)).save(priceCaptor.capture());
    Price savedPrice = priceCaptor.getValue();

    assertThat(savedPrice.getCoin()).isEqualTo("BTC");
    assertThat(savedPrice.getPriceDate()).isEqualTo(
        LocalDateTime.of(2022, Month.JANUARY, 14, 16, 0));
    assertThat(savedPrice.getPrice()).isEqualTo(new BigDecimal("40000.50"));

    verify(coinRepository, times(1)).addCoin("BTC");
  }

  @Test
  void testParseTimestamp() {
    String timestamp = "1620000000000";
    LocalDateTime dateTime = csvLoader.parseTimestamp(timestamp);

    assertThat(dateTime).isNotNull();
    assertThat(dateTime.getYear()).isEqualTo(2021);
    assertThat(dateTime.getMonthValue()).isEqualTo(5);
  }
}