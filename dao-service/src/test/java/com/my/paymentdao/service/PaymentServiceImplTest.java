package com.my.paymentdao.service;

import static org.mockito.ArgumentMatchers.any;

import com.my.paymentdao.dto.payment.PaymentBriefResponse;
import com.my.paymentdao.dto.payment.PaymentClientBriefResponse;
import com.my.paymentdao.dto.payment.PaymentRequest;
import com.my.paymentdao.dto.payment.PaymentResponse;
import com.my.paymentdao.mapper.PaymentMapper;
import com.my.paymentdao.persistence.domain.entity.Payment;
import com.my.paymentdao.persistence.repository.PaymentRepository;
import com.my.paymentdao.service.exception.EntityNotFoundException;
import com.my.paymentdao.service.impl.PaymentServiceImpl;
import com.my.paymentdao.util.DataGenerator;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
  @Mock
  private PaymentMapper paymentMapper;
  @Mock
  private PaymentRepository paymentRepository;
  @InjectMocks
  private PaymentServiceImpl paymentService;

  @Test
  void createPayment_ShouldSavePayment() {
    // given
    PaymentRequest paymentRequest = PaymentRequest.builder().build();
    Payment payment = DataGenerator.createValidPayment(1L);
    Mockito.when(paymentMapper.mapRequestToEntity(any(PaymentRequest.class)))
        .thenReturn(payment);
    Mockito.when(paymentRepository.save(any(Payment.class)))
        .thenReturn(payment);
    Mockito.when(paymentMapper.mapEntityToResponse(payment))
        .thenReturn(new PaymentResponse());
    // when
    PaymentResponse paymentResponse = paymentService.createPayment(paymentRequest);
    // then
    Mockito.verify(paymentRepository).save(payment);
    Assertions.assertThat(paymentResponse).isNotNull();
  }

  @Test
  void updatePayment_ShouldUpdatePayment() {
    // given
    PaymentRequest paymentRequest = PaymentRequest.builder().build();
    Payment payment = DataGenerator.createValidPayment(1L);
    Mockito.when(paymentRepository.findById(payment.getId()))
        .thenReturn(Optional.of(payment));
    Mockito.when(paymentMapper.mapEntityToResponse(payment))
        .thenReturn(new PaymentResponse());
    // when
    PaymentResponse paymentResponse = paymentService.updatePayment(payment.getId(), paymentRequest);
    // then
    Mockito.verify(paymentMapper).updateEntityWithRequest(payment, paymentRequest);
    Assertions.assertThat(paymentResponse).isNotNull();
  }

  @Test
  void getPaymentById() {
    // given
    Long paymentId = 1L;
    PaymentResponse payment = new PaymentResponse();
    Mockito.when(paymentRepository.findByIdAsPaymentResponse(paymentId))
        .thenReturn(Optional.of(payment));
    // when
    PaymentResponse paymentResponse = paymentService.getPaymentById(paymentId);
    // then
    Assertions.assertThat(paymentResponse).isNotNull();
  }

  @Test
  void getPayments_ShouldReturnPayments() {
    // given
    List<PaymentBriefResponse> expectedPayments = List.of(PaymentBriefResponse.builder().build(),
        PaymentBriefResponse.builder().build());
    Mockito.when(paymentRepository.findAllWithLastWithdrawalDateTime())
        .thenReturn(expectedPayments);
    // when
    List<PaymentBriefResponse> paymentResponses = paymentService.getPayments();
    // then
    Assertions.assertThat(paymentResponses.size()).isEqualTo(expectedPayments.size());
  }

  @Test
  void getPaymentsByInn() {
    // given
    String inn = "inn";
    List<Payment> expectedPayments = List.of(new Payment(), new Payment());
    Mockito.when(paymentRepository.findAllByPayerInn(inn))
        .thenReturn(expectedPayments);
    Mockito.when(paymentMapper.mapEntityToBriefResponse(any(Payment.class)))
        .thenReturn(PaymentClientBriefResponse.builder().build());
    // when
    List<PaymentClientBriefResponse> paymentResponses = paymentService.getPaymentsByInn(inn);
    // then
    Assertions.assertThat(paymentResponses.size()).isEqualTo(expectedPayments.size());
  }

  @Test
  void testGetPaymentsByOkpo() {
    // given
    String okpo = "okpo";
    List<Payment> expectedPayments = List.of(new Payment(), new Payment());
    Mockito.when(paymentRepository.findAllByRecipientOkpo(okpo))
        .thenReturn(expectedPayments);
    Mockito.when(paymentMapper.mapEntityToBriefResponse(any(Payment.class)))
        .thenReturn(new PaymentClientBriefResponse(1L, Duration.ZERO, BigDecimal.TEN));
    // when
    List<PaymentClientBriefResponse> paymentResponses = paymentService.getPaymentsByOkpo(okpo);
    // then
    Assertions.assertThat(paymentResponses.size()).isEqualTo(expectedPayments.size());
  }

  @Test
  void testDeletePaymentById_ShouldCallMethodToDeletePayment() {
    // given
    Payment payment = Payment.builder()
        .id(1L)
        .build();
    Mockito.when(paymentRepository.findById(any(Long.class)))
        .thenReturn(Optional.of(payment));
    // when
    paymentService.deletePaymentById(payment.getId());
    // then
    Mockito.verify(paymentRepository).delete(payment);
  }

  @Test
  void testGetPaymentEntityById_ShouldReturnPayment() {
    // given
    Mockito.when(paymentRepository.findById(any(Long.class)))
        .thenReturn(Optional.of(new Payment()));
    // when
    Payment payment = paymentService.getPaymentEntityById(1L);
    // then
    Assertions.assertThat(payment).isNotNull();
  }

  @Test
  void testGetPaymentEntityById_WhenPaymentByIdDoesntExist_ShouldThrowEntityNotFoundException() {
    // when and then
    Assertions.assertThatThrownBy(() -> paymentService.getPaymentById(1L))
        .isInstanceOf(EntityNotFoundException.class);
  }
}