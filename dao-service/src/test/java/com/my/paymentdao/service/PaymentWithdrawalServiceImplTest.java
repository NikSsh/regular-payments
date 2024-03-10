package com.my.paymentdao.service;

import static org.mockito.ArgumentMatchers.any;

import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import com.my.paymentdao.mapper.PaymentWithdrawalMapper;
import com.my.paymentdao.persistence.domain.entity.Payment;
import com.my.paymentdao.persistence.domain.entity.PaymentWithdrawal;
import com.my.paymentdao.persistence.domain.enums.PaymentWithdrawalStatus;
import com.my.paymentdao.persistence.repository.PaymentWithdrawalRepository;
import com.my.paymentdao.service.exception.EntityNotFoundException;
import com.my.paymentdao.service.impl.PaymentWithdrawalServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PaymentWithdrawalServiceImplTest {
  @Mock
  private PaymentService paymentService;
  @Mock
  private PaymentWithdrawalRepository paymentWithdrawalRepository;
  @Mock
  private PaymentWithdrawalMapper paymentWithdrawalMapper;
  @InjectMocks
  private PaymentWithdrawalServiceImpl paymentWithdrawalService;

  @Test
  void testGetById_ShouldReturnWithdrawalResponseById() {
    // given
    Long withdrawalId = 1L;
    PaymentWithdrawal paymentWithdrawalEntity = new PaymentWithdrawal();
    Mockito.when(paymentWithdrawalRepository.findById(withdrawalId))
        .thenReturn(Optional.of(paymentWithdrawalEntity));
    Mockito.when(paymentWithdrawalMapper.mapEntityToResponse(paymentWithdrawalEntity))
        .thenReturn(PaymentWithdrawalResponse.builder().build());
    // when
    PaymentWithdrawalResponse paymentWithdrawalResponse = paymentWithdrawalService
        .getById(withdrawalId);
    // then
    Assertions.assertThat(paymentWithdrawalResponse).isNotNull();
  }

  @Test
  void testGetById_WhenWithdrawalByIdDoesntExist_ShouldThrowEntityNotFoundException() {
    // given
    Long withdrawalId = 1L;
    Mockito.when(paymentWithdrawalRepository.findById(withdrawalId))
        .thenReturn(Optional.empty());
    // when and then
    Assertions.assertThatThrownBy(() -> paymentWithdrawalService.getById(withdrawalId))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void testChangePaymentWithdrawalStatusToStornowed_ShouldChangeWithdrawalStatusToStornowed() {
    // given
    Long withdrawalId = 1L;
    PaymentWithdrawal paymentWithdrawalEntity = new PaymentWithdrawal();
    Mockito.when(paymentWithdrawalRepository.findById(withdrawalId))
        .thenReturn(Optional.of(paymentWithdrawalEntity));
    // when
    paymentWithdrawalService.changePaymentWithdrawalStatusToStornowed(withdrawalId);
    // then
    Assertions.assertThat(paymentWithdrawalEntity.getStatus()).isEqualTo(PaymentWithdrawalStatus.S);
  }

  @Test
  void testChangePaymentWithdrawalStatusToStornowed_WhenWithdrawalByIdDoesntExist_ShouldThrowEntityNotFoundException() {
    // given
    Long withdrawalId = 1L;
    Mockito.when(paymentWithdrawalRepository.findById(withdrawalId))
        .thenReturn(Optional.empty());
    // when
    Assertions.assertThatThrownBy(() -> paymentWithdrawalService
            .changePaymentWithdrawalStatusToStornowed(withdrawalId))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void testGetAllByPaymentId_ShouldGetAll() {
    // given
    Long paymentId = 1L;
    List<PaymentWithdrawalResponse> expectedPaymentWithdrawalResponses = List.of(
        PaymentWithdrawalResponse.builder().build(),
        PaymentWithdrawalResponse.builder().build()
    );
    Mockito.when(paymentWithdrawalRepository.findAllByPaymentId(paymentId))
        .thenReturn(expectedPaymentWithdrawalResponses);
    // when
    List<PaymentWithdrawalResponse> result = paymentWithdrawalService.getAllByPaymentId(paymentId);
    Assertions.assertThat(result.size()).isEqualTo(expectedPaymentWithdrawalResponses.size());
  }

  @Test
  void testCreatePaymentWithdrawal_ShouldCreatePaymentWithdrawal() {
    // given
    Long paymentId = 1L;
    Payment payment = Payment.builder()
        .amount(BigDecimal.TEN)
        .build();
    Mockito.when(paymentService.getPaymentEntityById(paymentId))
        .thenReturn(payment);
    ArgumentCaptor<PaymentWithdrawal> argumentCaptor = ArgumentCaptor.forClass(PaymentWithdrawal.class);
    PaymentWithdrawal paymentWithdrawalFromRepository = new PaymentWithdrawal();
    Mockito.when(paymentWithdrawalRepository.save(argumentCaptor.capture()))
        .thenReturn(paymentWithdrawalFromRepository);
    Mockito.when(paymentWithdrawalMapper.mapEntityToResponse(paymentWithdrawalFromRepository))
        .thenReturn(PaymentWithdrawalResponse.builder().build());
    // when
    PaymentWithdrawalResponse paymentWithdrawalResponse = paymentWithdrawalService
        .createPaymentWithdrawal(paymentId);
    // then
    PaymentWithdrawal savedPaymentWithdrawal = argumentCaptor.getValue();
    Assertions.assertThat(savedPaymentWithdrawal.getPayment()).isEqualTo(payment);
    Assertions.assertThat(savedPaymentWithdrawal.getStatus()).isEqualTo(PaymentWithdrawalStatus.A);
    Assertions.assertThat(savedPaymentWithdrawal.getAmount()).isEqualTo(payment.getAmount());
    Assertions.assertThat(paymentWithdrawalResponse).isNotNull();
  }
}