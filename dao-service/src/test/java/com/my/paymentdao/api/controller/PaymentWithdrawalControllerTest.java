package com.my.paymentdao.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import com.my.paymentdao.persistence.domain.enums.PaymentWithdrawalStatus;
import com.my.paymentdao.service.PaymentWithdrawalService;
import com.my.paymentdao.service.exception.EntityNotFoundException;
import java.math.BigDecimal;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PaymentWithdrawalController.class)
@ExtendWith(MockitoExtension.class)
class PaymentWithdrawalControllerTest {
  private static final String PAYMENT_WITHDRAWAL_CONTROLLER_URL = "/api/v1/payment-withdrawals";
  private static final ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private PaymentWithdrawalService paymentWithdrawalService;

  @SneakyThrows
  @Test
  void testGetPaymentWithdrawal_ShouldReturnWithdrawalAndResponseWithHttpStatusOk() {
    PaymentWithdrawalResponse paymentWithdrawalResponse = PaymentWithdrawalResponse.builder()
        .id(1L)
        .amount(BigDecimal.TEN)
        .status(PaymentWithdrawalStatus.A)
        .build();
    Mockito.when(paymentWithdrawalService.getById(paymentWithdrawalResponse.id()))
        .thenReturn(paymentWithdrawalResponse);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(PAYMENT_WITHDRAWAL_CONTROLLER_URL + "/" + paymentWithdrawalResponse.id()))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(paymentWithdrawalResponse)));
  }

  @SneakyThrows
  @Test
  void getPaymentWithdrawal_WhenEntityNotFoundExceptionIsThrown_ShouldResponseWithHttpStatusNotFound() {
    Long withdrawalId = 1L;
    Mockito.when(paymentWithdrawalService.getById(withdrawalId))
        .thenThrow(new EntityNotFoundException("Entity by id was not found"));
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(PAYMENT_WITHDRAWAL_CONTROLLER_URL + "/" + withdrawalId))
        .andExpect(status().isNotFound());
  }

  @Test
  void deletePaymentWithdrawal_ShouldChangePaymentWithdrawalStatusAndResponseWithHttpStatusOk()
      throws Exception {
    Long withdrawalId = 1L;
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .delete(PAYMENT_WITHDRAWAL_CONTROLLER_URL + "/" + withdrawalId))
        .andExpect(status().isOk());
    Mockito.verify(paymentWithdrawalService).changePaymentWithdrawalStatusToStornowed(withdrawalId);
  }
}