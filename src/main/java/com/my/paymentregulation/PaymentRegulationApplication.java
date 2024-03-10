package com.my.paymentregulation;

import com.my.paymentregulation.service.PaymentWithdrawalService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@AllArgsConstructor
@SpringBootApplication
@EnableFeignClients
@Slf4j
public class PaymentRegulationApplication implements ApplicationRunner {
  private PaymentWithdrawalService paymentWithdrawalService;

  public static void main(String[] args) {
    SpringApplication.run(PaymentRegulationApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) {
    try {
      paymentWithdrawalService.initiatePaymentWithdrawalProcess();
    } catch (FeignException e) {
      log.error(e.getMessage());
    }
  }
}
