package com.checkout.payment.gateway.configuration;

import java.time.Duration;
import com.checkout.payment.gateway.client.AcquirerClient;
import com.checkout.payment.gateway.client.GatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {

  @Autowired
  private Environment env;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofMillis(10000))
        .setReadTimeout(Duration.ofMillis(10000))
        .build();
  }

  @Bean
  public AcquirerClient acquirerClient() {
    return new AcquirerClient(
        env.getProperty("acquirer.baseUri"),
        env.getProperty("acquirer.port"));
  }

  @Bean
  public GatewayClient gatewayClient() {
    return new GatewayClient(
        env.getProperty("gateway.baseUri"),
        env.getProperty("gateway.port"));
  }
}
