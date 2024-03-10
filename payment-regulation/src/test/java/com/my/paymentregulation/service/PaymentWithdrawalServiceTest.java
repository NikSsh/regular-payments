package com.my.paymentregulation.service;

import com.my.paymentregulation.client.PaymentBusinessLogicClient;
import com.my.paymentregulation.dto.PaymentBriefResponse;
import com.my.paymentregulation.util.DataGenerator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PaymentWithdrawalServiceTest {
  @Mock
  private PaymentBusinessLogicClient paymentBusinessLogicClient;
  @InjectMocks
  private PaymentWithdrawalService paymentWithdrawalService;

  @Test
  void testInitiatePaymentWithdrawalProcess_WhenPaymentListNeedsWithdrawal_ShouldCreatePaymentWithdrawals() {
    // given
    List<PaymentBriefResponse> payments = DataGenerator
        .generatePaymentBriefResponses(30, true);
    Mockito.when(paymentBusinessLogicClient.getPayments())
        .thenReturn(ResponseEntity.ok(payments));
    // when
    paymentWithdrawalService.initiatePaymentWithdrawalProcess();
    // then
    payments.parallelStream()
        .forEach(paymentBriefResponse ->
            Mockito.verify(paymentBusinessLogicClient)
                .createPaymentWithdrawal(paymentBriefResponse.id()));
  }

  @Test
  void testInitiatePaymentWithdrawalProcess_WhenPaymentListDoesntNeedWithdrawal_ShouldNotCreateAnyPaymentWithdrawal() {
    // given
    List<PaymentBriefResponse> payments = DataGenerator
        .generatePaymentBriefResponses(30, false);
    Mockito.when(paymentBusinessLogicClient.getPayments())
        .thenReturn(ResponseEntity.ok(payments));
    // when
    paymentWithdrawalService.initiatePaymentWithdrawalProcess();
    // then
    payments.parallelStream()
        .forEach(paymentBriefResponse ->
            Mockito.verify(paymentBusinessLogicClient, Mockito.never())
                .createPaymentWithdrawal(paymentBriefResponse.id()));
  }
}