package com.my.paymentdao.service;

import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import java.util.List;

public interface PaymentWithdrawalService {
  PaymentWithdrawalResponse getById(Long id);

  void changePaymentWithdrawalStatusToStornowed(Long id);

  List<PaymentWithdrawalResponse> getAllByPaymentId(Long paymentId);

  PaymentWithdrawalResponse createPaymentWithdrawal(Long paymentId);
}
