package com.my.paymentdao.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.my.paymentdao.persistence.domain.entity.Payment;
import com.my.paymentdao.persistence.domain.entity.PaymentWithdrawal;
import com.my.paymentdao.persistence.domain.enums.PaymentWithdrawalStatus;
import com.my.paymentdao.persistence.repository.PaymentRepository;
import com.my.paymentdao.persistence.repository.PaymentWithdrawalRepository;
import com.my.paymentdao.util.DataGenerator;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentWithdrawalControllerIT {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PaymentRepository paymentRepository;
  @Autowired
  private PaymentWithdrawalRepository paymentWithdrawalRepository;
  @LocalServerPort
  private int port;

  private String createURLWithPort() {
    return "http://localhost:" + port + "/api/v1/payment-withdrawals";
  }

  @SneakyThrows
  @Test
  void testGetPaymentWithdrawal_ShouldReturnWithdrawalAndResponseWithHttpStatusOk() {
    Payment payment = DataGenerator.createValidPayment(1L);
    payment = paymentRepository.save(payment);
    PaymentWithdrawal paymentWithdrawal = DataGenerator.createPaymentWithdrawal(1L, payment);
    paymentWithdrawal = paymentWithdrawalRepository.save(paymentWithdrawal);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(createURLWithPort() + "/" + paymentWithdrawal.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpectAll(
            jsonPath("$.id").value(paymentWithdrawal.getId()),
            jsonPath("$.amount").value(paymentWithdrawal.getAmount())
        );
  }

  @SneakyThrows
  @Test
  void deletePaymentWithdrawal_ShouldChangePaymentWithdrawalStatusAndResponseWithHttpStatusOk() {
    Payment payment = DataGenerator.createValidPayment(1L);
    payment = paymentRepository.save(payment);
    PaymentWithdrawal paymentWithdrawal = DataGenerator.createPaymentWithdrawal(1L, payment);
    paymentWithdrawal = paymentWithdrawalRepository.save(paymentWithdrawal);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .delete(createURLWithPort() + "/" + paymentWithdrawal.getId()))
        .andExpect(status().isOk());

    Assertions.assertThat(paymentWithdrawalRepository.findById(paymentWithdrawal.getId())
        .get().getStatus()).isEqualTo(PaymentWithdrawalStatus.S);
  }
}
