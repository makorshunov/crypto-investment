package com.crypto.investment.common;

import com.crypto.investment.exception.RateLimitException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Helps to control the number of requests that can be made to the application from a single IP
 * address within a specified time window.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

  private final Map<String, RateLimitInterceptor.RequestCounter> requestCounts = new ConcurrentHashMap<>();

  /**
   * The maximum number of requests allowed per time window.
   */
  @Value("${rate.limit.per.window}")
  private int maxPerTimeWindow = 10;

  /**
   * The duration of the time window in milliseconds.
   */
  @Value("${rate.limit.time.window}")
  private long timeWindow = 60000;


  /**
   * This method is called before the request is handled by the controller. It checks the number of
   * requests made by the client IP address within the time window. If the limit is exceeded, a
   * {@link RateLimitException} is thrown.
   *
   * @param request  The {@link HttpServletRequest} object that contains the request details.
   * @param response The {@link HttpServletResponse} object that will be used to send the response.
   * @param handler  The handler (controller) that will handle the request.
   * @return {@code true} if the request should be processed; {@code false} otherwise.
   * @throws RateLimitException if the rate limit is exceeded.
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    String clientIP = request.getRemoteAddr();
    RateLimitInterceptor.RequestCounter counter = requestCounts.computeIfAbsent(clientIP,
        k -> new RateLimitInterceptor.RequestCounter());

    synchronized (counter) {
      long currentTime = Instant.now().toEpochMilli();

      if (currentTime - counter.timestamp > timeWindow) {
        counter.timestamp = currentTime;
        counter.requests = 0;
      }
      counter.requests++;
      if (counter.requests > maxPerTimeWindow) {
        throw new RateLimitException();
      }
    }
    return true;
  }

  /**
   * A helper class to track the number of requests and the timestamp for each client IP address.
   */ 
  private static class RequestCounter {

    long timestamp;
    int requests;
  }
}
