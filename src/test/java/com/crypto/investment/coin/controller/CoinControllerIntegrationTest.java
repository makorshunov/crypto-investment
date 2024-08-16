package com.crypto.investment.coin.controller;

import com.crypto.investment.coin.model.CoinInfoDto;
import com.crypto.investment.coin.persistence.CoinInfoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CoinControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CoinInfoRepository coinInfoRepository;

  private CoinInfoDto coinInfoDto;

  @BeforeEach
  void setUp() {
    coinInfoDto = new CoinInfoDto("BTC", new BigDecimal("100.00"), new BigDecimal("10.00"), LocalDateTime.of(2022,
        Month.JANUARY, 1, 0, 0),  LocalDateTime.of(2022,
        Month.JANUARY, 30, 0, 0));
  }

  @Test
  void testGetCoinInfo() throws Exception {
    when(coinInfoRepository.getCoinInfoByCoin(eq("BTC"))).thenReturn(coinInfoDto);

    mockMvc.perform(get("/api/v1/coin/info/BTC"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.coin", is("BTC")))
        .andExpect(jsonPath("$.max", is(100.00)))
        .andExpect(jsonPath("$.min", is(10.00)))
        .andExpect(jsonPath("$.oldest", is("2022-01-01T00:00:00")))
        .andExpect(jsonPath("$.newest", is("2022-01-30T00:00:00")));
  }

  @Test
  void testGetCoinListNormalizedRange() throws Exception {
    List<String> coinList = Arrays.asList("BTC", "ETH", "XRP");
    when(coinInfoRepository.getCoinListSortedByNormalizedRange()).thenReturn(coinList);

    mockMvc.perform(get("/api/v1/coin/range"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0]", is("BTC")))
        .andExpect(jsonPath("$[1]", is("ETH")))
        .andExpect(jsonPath("$[2]", is("XRP")));
  }

  @Test
  void testGetCoinWithMaxNormalizedRange() throws Exception {
    when(coinInfoRepository.getCoinWithMaxNormalizedRange(any(LocalDate.class))).thenReturn("BTC");

    mockMvc.perform(get("/api/v1/coin/max/01-01-2022"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("BTC")));
  }
}
