package com.my.paymentdao.persistence.repository;

import com.my.paymentdao.dto.payment.PaymentBriefResponse;
import com.my.paymentdao.dto.payment.PaymentResponse;
import com.my.paymentdao.persistence.domain.entity.Payment;
import com.my.paymentdao.persistence.domain.entity.PaymentWithdrawal;
import com.my.paymentdao.util.DataGenerator;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class PaymentRepositoryTest {
  @Autowired
  private PaymentRepository paymentRepository;
  @Autowired
  private PaymentWithdrawalRepository paymentWithdrawalRepository;

  private Payment storedPayment;
  private List<PaymentWithdrawal> storedPaymentWithdrawals;

  @BeforeEach
  void setUp() {
    storedPayment = DataGenerator.createValidPayment(1L);
    storedPayment = paymentRepository.save(storedPayment);

    storedPaymentWithdrawals = DataGenerator.createPaymentWithdrawals(storedPayment, 5);
    storedPaymentWithdrawals = paymentWithdrawalRepository.saveAll(storedPaymentWithdrawals);
  }

  @Test
  void findByIdAsPaymentResponse_ShouldReturnValidPaymentResponse() {
    // when
    Optional<PaymentResponse> optionalPaymentResponse = paymentRepository
        .findByIdAsPaymentResponse(storedPayment.getId());
    // then
    Assertions.assertThat(optionalPaymentResponse.isPresent()).isTrue();

    PaymentResponse paymentResponse = optionalPaymentResponse.get();
    Assertions.assertThat(paymentResponse.getId()).isEqualTo(storedPayment.getId());

    PaymentWithdrawal paymentWithdrawalWithMaxCreatedAt = storedPaymentWithdrawals.stream()
        .max(Comparator.comparing(PaymentWithdrawal::getCreatedAt)).get();
    Assertions.assertThat(paymentResponse.getLastWithdrawal())
        .isEqualTo(paymentWithdrawalWithMaxCreatedAt.getCreatedAt());
  }

  @Test
  void findByIdAsPaymentResponse_WhenPaymentByIdDoesntExist_ShouldReturnEmptyOptional() {
    // when
    Optional<PaymentResponse> optionalPaymentResponse = paymentRepository
        .findByIdAsPaymentResponse(storedPayment.getId() + 1);
    // then
    Assertions.assertThat(optionalPaymentResponse.isPresent()).isFalse();
  }

  @Test
  void findAllAsBriefResponse() {
    // when
    List<PaymentBriefResponse> briefResponses = paymentRepository
        .findAllWithLastWithdrawalDateTime();
    // then
    Assertions.assertThat(briefResponses.size()).isEqualTo(1);

    PaymentBriefResponse paymentResponse = briefResponses.get(0);
    Assertions.assertThat(paymentResponse.id()).isEqualTo(storedPayment.getId());

    PaymentWithdrawal paymentWithdrawalWithMaxCreatedAt = storedPaymentWithdrawals.stream()
        .max(Comparator.comparing(PaymentWithdrawal::getCreatedAt)).get();
    Assertions.assertThat(paymentResponse.lastWithdrawal())
        .isEqualTo(paymentWithdrawalWithMaxCreatedAt.getCreatedAt());
  }
}