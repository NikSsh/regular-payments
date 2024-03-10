package com.my.paymentdao.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.paymentdao.dto.payment.PaymentBriefResponse;
import com.my.paymentdao.dto.payment.PaymentClientBriefResponse;
import com.my.paymentdao.dto.payment.PaymentRequest;
import com.my.paymentdao.dto.payment.PaymentResponse;
import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import com.my.paymentdao.service.PaymentService;
import com.my.paymentdao.service.PaymentWithdrawalService;
import com.my.paymentdao.service.exception.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PaymentController.class)
@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {
  private static final String PAYMENT_CONTROLLER_URL = "/api/v1/payments";
  private static final ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private PaymentService paymentService;
  @MockBean
  private PaymentWithdrawalService paymentWithdrawalService;

  @SneakyThrows
  @Test
  void testCreatePayment_ShouldCreatePaymentAndResponseWithHttpStatusCreated() {
    PaymentRequest paymentRequest = PaymentRequest.builder()
        .build();
    PaymentResponse paymentResponse = PaymentResponse.builder()
        .id(1L)
        .amount(BigDecimal.TEN)
        .build();
    Mockito.when(paymentService.createPayment(paymentRequest))
        .thenReturn(paymentResponse);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .post(PAYMENT_CONTROLLER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(paymentRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(paymentResponse)));
  }

  @SneakyThrows
  @Test
  void testCreatePayment_WhenMalformedJsonRequestShouldResponseWithHttpStatusBadRequest() {
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .post(PAYMENT_CONTROLLER_URL)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @SneakyThrows
  @Test
  void testUpdatePayment_ShouldUpdatePaymentAndResponseWithHttpStatusOk() {
    Long paymentId = 1L;
    PaymentRequest paymentRequest = PaymentRequest.builder()
        .recipientName("RecipientName")
        .build();
    PaymentResponse paymentResponse = PaymentResponse.builder()
        .id(paymentId)
        .amount(BigDecimal.TEN)
        .build();
    Mockito.when(paymentService.updatePayment(paymentId, paymentRequest))
        .thenReturn(paymentResponse);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .put(PAYMENT_CONTROLLER_URL + "/" + paymentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(paymentRequest)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(paymentResponse)));
  }

  @SneakyThrows
  @Test
  void testGetAll_ShouldReturnAllPaymentsWithHttpStatusOk() {
    List<PaymentBriefResponse> paymentResponseList = List.of(PaymentBriefResponse.builder()
        .id(1L)
        .build());
    Mockito.when(paymentService.getPayments())
        .thenReturn(paymentResponseList);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(PAYMENT_CONTROLLER_URL))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(paymentResponseList)));
  }

  @SneakyThrows
  @Test
  void testGetAllByInn_ShouldReturnAllPaymentsByInnWithHttpStatusOk() {
    String inn = "1246993119";
    List<PaymentClientBriefResponse> paymentResponseList = List.of(PaymentClientBriefResponse.builder()
        .id(1L)
        .build());
    Mockito.when(paymentService.getPaymentsByInn(inn))
        .thenReturn(paymentResponseList);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(PAYMENT_CONTROLLER_URL + "/inn/" + inn))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(paymentResponseList)));
  }

  @SneakyThrows
  @Test
  void testGetAllByOkpo_ShouldReturnAllPaymentsByOkpoWithHttpStatusOk() {
    String okpo = "1246993119";
    List<PaymentClientBriefResponse> paymentResponseList = List.of(PaymentClientBriefResponse.builder()
        .id(1L)
        .build());
    Mockito.when(paymentService.getPaymentsByOkpo(okpo))
        .thenReturn(paymentResponseList);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(PAYMENT_CONTROLLER_URL + "/okpo/" + okpo))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(paymentResponseList)));
  }

  @SneakyThrows
  @Test
  void testGetById_ShouldReturnPaymentByIdWithHttpStatusOk() {
    long paymentId = 1L;
    PaymentResponse paymentResponse = PaymentResponse.builder()
        .id(paymentId)
        .amount(BigDecimal.TEN)
        .build();
    Mockito.when(paymentService.getPaymentById(paymentId))
        .thenReturn(paymentResponse);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(PAYMENT_CONTROLLER_URL + "/" + paymentId))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(paymentResponse)));
  }

  @SneakyThrows
  @Test
  void testGetById_WhenEntityNotFoundExceptionIsThrown_ShouldResponseWithHttpStatusNotFound() {
    long paymentId = 1L;
    Mockito.when(paymentService.getPaymentById(paymentId))
        .thenThrow(new EntityNotFoundException("Entity was not found!"));
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(PAYMENT_CONTROLLER_URL + "/" + paymentId))
        .andExpect(status().isNotFound());
  }

  @SneakyThrows
  @Test
  void testDeleteById() {
    long paymentId = 1L;
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .delete(PAYMENT_CONTROLLER_URL + "/" + paymentId))
        .andExpect(status().isOk());
    Mockito.verify(paymentService).deletePaymentById(paymentId);
  }

  @SneakyThrows
  @Test
  void testCreatePaymentWithdrawal() {
    long paymentId = 1L;
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .post(PAYMENT_CONTROLLER_URL + "/" + paymentId + "/withdrawals"))
        .andExpect(status().isCreated());
    Mockito.verify(paymentWithdrawalService).createPaymentWithdrawal(paymentId);
  }

  @SneakyThrows
  @Test
  void testGetPaymentWithdrawals() {
    long paymentId = 1L;
    List<PaymentWithdrawalResponse> paymentWithdrawals = List.of(PaymentWithdrawalResponse.builder()
        .id(1L)
        .build());
    Mockito.when(paymentWithdrawalService.getAllByPaymentId(paymentId))
            .thenReturn(paymentWithdrawals);
    // when and then
    mockMvc.perform(MockMvcRequestBuilders
            .get(PAYMENT_CONTROLLER_URL + "/" + paymentId + "/withdrawals"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(paymentWithdrawals)));
  }
}