package com.crypto.investment.utils;

import com.crypto.investment.coin.persistence.CoinRepository;
import com.crypto.investment.exception.CSVImportException;
import com.crypto.investment.exception.CSVParsingException;
import com.crypto.investment.price.model.PriceDto;
import com.crypto.investment.price.persistence.Price;
import com.crypto.investment.price.persistence.PriceRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * The {@code CSVLoader} class is responsible for loading and processing CSV files containing
 * cryptocurrency price data.
 */
@Component
@Slf4j
public class CSVLoader {

  private static final String SYMBOL = "symbol";
  private static final String TIMESTAMP = "timestamp";
  private static final String PRICE = "price";

  private final PriceRepository priceRepository;
  private final CoinRepository coinRepository;
  private final ResourcePatternResolver resourcePatternResolver;

  @Autowired
  public CSVLoader(PriceRepository priceRepository, CoinRepository coinRepository,
      ResourcePatternResolver resourcePatternResolver) {
    this.priceRepository = priceRepository;
    this.coinRepository = coinRepository;
    this.resourcePatternResolver = resourcePatternResolver;
  }

  /**
   * Event handler that triggers the loading of CSV files when the application context is
   * refreshed.
   *
   * @param event the event triggered when the application context is refreshed
   */
  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("Loading crypto information");
    List<PriceDto> priceDtoList = loadAllCSV();
    priceDtoList.forEach(e -> {

      Price price = new Price();
      price.setPriceDate(e.dateTime());
      price.setPrice(e.price());
      if (!coinRepository.containsCoin(e.coin())) {
        coinRepository.addCoin(e.coin());
      }
      price.setCoin(e.coin());
      priceRepository.save(price);
    });
  }

  /**
   * Loads and processes all CSV files from the 'prices' directory.
   *
   * @return a list of {@link PriceDto} objects parsed from the CSV files
   * @throws CSVImportException if the CSV files could not be loaded
   */
  public List<PriceDto> loadAllCSV() {
    List<PriceDto> arr = new ArrayList<>();
    try {
      Resource[] resourcesArray = resourcePatternResolver.getResources("classpath:prices/*.csv");
      for (Resource resource : resourcesArray) {
        if (resource.exists() && resource.isReadable()) {
          arr.addAll(loadFromCSVFile(resource));
        }
      }
    } catch (IOException e) {
      throw new CSVImportException("Failed to load resources from 'prices' directory");
    }

    return arr;
  }

  /**
   * Parses a single CSV file and converts its content into a list of {@link PriceDto} objects.
   *
   * @param resource the CSV file to be parsed
   * @return a list of {@link PriceDto} objects representing the data in the CSV file
   * @throws CSVParsingException if an error occurs while parsing the CSV file
   */
  public List<PriceDto> loadFromCSVFile(Resource resource) {
    CSVFormat CSVFormat = Builder.create().setHeader(TIMESTAMP, SYMBOL, PRICE)
        .setSkipHeaderRecord(true).build();

    try (BufferedReader bReader = new BufferedReader(
        new InputStreamReader(resource.getInputStream(),
            StandardCharsets.UTF_8)); CSVParser csvParser = new CSVParser(bReader, CSVFormat)) {

      List<PriceDto> priceDtoList = new ArrayList<>();
      for (CSVRecord csvRecord : csvParser.getRecords()) {
        LocalDateTime dateTime = parseTimestamp(csvRecord.get(TIMESTAMP));
        BigDecimal price = new BigDecimal(csvRecord.get(PRICE));
        priceDtoList.add(new PriceDto(dateTime, csvRecord.get(SYMBOL), price));
      }
      return priceDtoList;
    } catch (IOException e) {
      throw new CSVParsingException("CSV data is failed to parse: " + e.getMessage());
    }
  }

  /**
   * Parses a timestamp from a string representing the number of milliseconds since the epoch.
   *
   * @param timestamp the string representation of the timestamp in milliseconds
   * @return a {@link LocalDateTime}
   */
  LocalDateTime parseTimestamp(String timestamp) {
    Instant instant = Instant.ofEpochMilli(Long.parseLong(timestamp));
    return LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
  }
}
