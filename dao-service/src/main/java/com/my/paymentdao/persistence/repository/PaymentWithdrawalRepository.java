package com.my.paymentdao.persistence.repository;

import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import com.my.paymentdao.persistence.domain.entity.PaymentWithdrawal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentWithdrawalRepository extends JpaRepository<PaymentWithdrawal, Long> {
  @Query("SELECT new com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse( "
      + "pw.id, pw.amount, pw.createdAt, pw.status) "
      + "FROM PaymentWithdrawal pw "
      + "WHERE pw.payment.id = ?1 "
      + "ORDER BY pw.createdAt DESC")
  List<PaymentWithdrawalResponse> findAllByPaymentId(Long paymentId);
}


