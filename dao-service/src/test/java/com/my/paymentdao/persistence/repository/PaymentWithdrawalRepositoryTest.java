package com.my.paymentdao.persistence.repository;

import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import com.my.paymentdao.persistence.domain.entity.Payment;
import com.my.paymentdao.persistence.domain.entity.PaymentWithdrawal;
import com.my.paymentdao.util.DataGenerator;
import java.util.List;
import lombok.AllArgsConstructor;
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
class PaymentWithdrawalRepositoryTest {
  @Autowired
  private PaymentRepository paymentRepository;
  @Autowired
  private PaymentWithdrawalRepository paymentWithdrawalRepository;

  @Test
  void testFindAllByPaymentId_ShouldReturnPaymentWithdrawals() {
    // given
    Payment storedPayment = DataGenerator.createValidPayment(1L);
    storedPayment = paymentRepository.save(storedPayment);

    PaymentWithdrawal firstPaymentWithdrawal = DataGenerator.createPaymentWithdrawal(1L, storedPayment);
    firstPaymentWithdrawal = paymentWithdrawalRepository.save(firstPaymentWithdrawal);

    Payment newStoredPayment = DataGenerator.createValidPayment(2L);
    newStoredPayment = paymentRepository.save(newStoredPayment);

    PaymentWithdrawal secondPaymentWithdrawal = DataGenerator.createPaymentWithdrawal(2L, newStoredPayment);
    secondPaymentWithdrawal = paymentWithdrawalRepository.save(secondPaymentWithdrawal);
    // when
    List<PaymentWithdrawalResponse> paymentWithdrawalResponses = paymentWithdrawalRepository
        .findAllByPaymentId(storedPayment.getId());
    // then
    Assertions.assertThat(paymentWithdrawalResponses.size()).isEqualTo(1);
    Assertions.assertThat(paymentWithdrawalResponses.get(0).id())
        .isEqualTo(firstPaymentWithdrawal.getId());
  }
}