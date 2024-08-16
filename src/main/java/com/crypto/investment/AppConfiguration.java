package com.crypto.investment;

import com.crypto.investment.common.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for the Spring Boot application.
 */
@Configuration
public class AppConfiguration implements WebMvcConfigurer {

  private final RateLimitInterceptor rateLimitInterceptor;

  @Autowired
  public AppConfiguration(RateLimitInterceptor rateLimitingInterceptor) {
    this.rateLimitInterceptor = rateLimitingInterceptor;
  }

  /**
   * Adds {@link RateLimitInterceptor} to the request handling chain
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**");
  }
}
