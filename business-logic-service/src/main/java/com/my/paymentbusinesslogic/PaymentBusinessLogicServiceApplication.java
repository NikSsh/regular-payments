package com.my.paymentbusinesslogic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main application class.
 */
@SpringBootApplication
@EnableFeignClients
public class PaymentBusinessLogicServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PaymentBusinessLogicServiceApplication.class, args);
  }

}
