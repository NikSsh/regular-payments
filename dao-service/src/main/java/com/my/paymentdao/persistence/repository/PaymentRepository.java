package com.my.paymentdao.persistence.repository;

import com.my.paymentdao.dto.payment.PaymentBriefResponse;
import com.my.paymentdao.dto.payment.PaymentResponse;
import com.my.paymentdao.persistence.domain.entity.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  // It would be better to use in such case:
  // LEFT JOIN (
  //   SELECT pw.payment_id, MAX(pw.createdAt) as lastWithdrawal
  //   FROM PaymentWithdrawal pw
  //   GROUP BY pw.payment_id
  //) pw ON p.id = pw.payment_id
  @Query("SELECT new com.my.paymentdao.dto.payment.PaymentResponse( "
      + "p.id, p.payerFullName, p.payerInn, p.payerCardNumber, p.recipientAccount, "
      + "p.recipientMfo, p.recipientOkpo, p.recipientName, p.amount, p.withdrawalPeriod, "
      + "MAX(pw.createdAt), p.createdAt) "
      + "FROM Payment p "
      + "LEFT JOIN PaymentWithdrawal pw ON p.id = pw.payment.id "
      + "WHERE p.id = ?1 "
      + "GROUP BY p.id")
  Optional<PaymentResponse> findByIdAsPaymentResponse(Long id);

  List<Payment> findAllByPayerInn(String inn);

  List<Payment> findAllByRecipientOkpo(String okpo);

  @Query(nativeQuery = true)
  List<PaymentBriefResponse> findAllWithLastWithdrawalDateTime();
}
