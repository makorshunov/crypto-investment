package com.crypto.investment.coin.persistence;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * The {@code CoinRepository} class is a Spring-managed component responsible for managing a set of
 * supported cryptocurrency symbols (coins). It provides methods to check if a coin is supported and
 * to add new coins to the repository.
 */
@Component
public class CoinRepository {

  /**
   * A thread-safe set to store unique cryptocurrency symbols (coins).
   */
  private final Set<String> threadSafeUniqueNumbers = ConcurrentHashMap.newKeySet();

  /**
   * Checks if a given coin is present in the repository.
   *
   * @param coin The symbol of the coin to check.
   * @return {@code true} if the coin is present in the repository; {@code false} otherwise.
   */
  public boolean containsCoin(String coin) {
    return threadSafeUniqueNumbers.contains(coin.toUpperCase());
  }

  /**
   * Adds a new coin to the repository.
   *
   * @param coin The symbol of the coin to add.
   */
  public void addCoin(String coin) {
    threadSafeUniqueNumbers.add(coin.toUpperCase());
  }
}
