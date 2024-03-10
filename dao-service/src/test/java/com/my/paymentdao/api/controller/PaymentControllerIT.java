package com.my.paymentdao.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.my.paymentdao.dto.payment.PaymentBriefResponse;
import com.my.paymentdao.dto.payment.PaymentClientBriefResponse;
import com.my.paymentdao.dto.payment.PaymentRequest;
import com.my.paymentdao.dto.payment.PaymentResponse;
import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import com.my.paymentdao.persistence.domain.entity.Payment;
import com.my.paymentdao.persistence.domain.entity.PaymentWithdrawal;
import com.my.paymentdao.persistence.repository.PaymentRepository;
import com.my.paymentdao.persistence.repository.PaymentWithdrawalRepository;
import com.my.paymentdao.service.exception.EntityNotFoundException;
import com.my.paymentdao.util.DataGenerator;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Period;
import java.util.List;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerIT {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PaymentRepository paymentRepository;
  @Autowired
  private PaymentWithdrawalRepository paymentWithdrawalRepository;

  @LocalServerPort
  private int port;

  private String createURLWithPort() {
    return "http://localhost:" + port + "/api/v1/payments";
  }

  @SneakyThrows
  @Test
  void testGetAll_ShouldReturnAllPaymentsWithHttpStatusOk() {
    Payment payment = DataGenerator.createValidPayment(1L);
    payment = paymentRepository.save(payment);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(createURLWithPort()))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$[0].id").value(payment.getId())
        );
  }

  @SneakyThrows
  @Test
  void testGetAllByInn_ShouldReturnAllPaymentsByInnWithHttpStatusOk() {
    Payment payment = DataGenerator.createValidPayment(1L);
    payment = paymentRepository.save(payment);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(createURLWithPort() + "/inn/" + payment.getPayerInn()))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$[0].id").value(payment.getId()),
            jsonPath("$[0].amount").value(payment.getAmount())
        );
  }

  @SneakyThrows
  @Test
  void testGetAllByOkpo_ShouldReturnAllPaymentsByOkpoWithHttpStatusOk() {
    Payment payment = DataGenerator.createValidPayment(1L);
    payment = paymentRepository.save(payment);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(createURLWithPort() + "/okpo/" + payment.getRecipientOkpo()))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$[0].id").value(payment.getId()),
            jsonPath("$[0].amount").value(payment.getAmount())
        );
  }

  @SneakyThrows
  @Test
  void testGetById_ShouldReturnPaymentByIdWithHttpStatusOk() {
    Payment payment = DataGenerator.createValidPayment(1L);
    payment = paymentRepository.save(payment);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(createURLWithPort() + "/" + payment.getId()))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.id").value(payment.getId()),
            jsonPath("$.amount").value(payment.getAmount())
        );
  }

  @SneakyThrows
  @Test
  void testDeleteById() {
    Payment payment = DataGenerator.createValidPayment(1L);
    payment = paymentRepository.save(payment);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .delete(createURLWithPort() + "/" + payment.getId()))
        .andExpect(status().isOk());
    Assertions.assertThat(paymentRepository.existsById(payment.getId()))
        .isFalse();
  }

  @SneakyThrows
  @Test
  void testCreatePaymentWithdrawal() {
    Payment payment = DataGenerator.createValidPayment(1L);
    payment = paymentRepository.save(payment);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .post(createURLWithPort() + "/" + payment.getId() + "/withdrawals"))
        .andExpect(status().isCreated());
    Assertions.assertThat(paymentWithdrawalRepository.findAllByPaymentId(payment.getId()).isEmpty())
        .isFalse();
  }

  @SneakyThrows
  @Test
  void testGetPaymentWithdrawals() {
    Payment payment = DataGenerator.createValidPayment(1L);
    payment = paymentRepository.save(payment);
    PaymentWithdrawal paymentWithdrawal = DataGenerator.createPaymentWithdrawal(1L, payment);
    paymentWithdrawal = paymentWithdrawalRepository.save(paymentWithdrawal);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(createURLWithPort() + "/" + payment.getId() + "/withdrawals"))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$[0].id").value(paymentWithdrawal.getId()),
            jsonPath("$[0].amount").value(paymentWithdrawal.getAmount())
        );
  }
}
