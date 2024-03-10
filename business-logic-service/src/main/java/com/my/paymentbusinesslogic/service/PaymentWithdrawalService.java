package com.my.paymentbusinesslogic.service;

import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentWithdrawalResponse;
import java.util.List;

/**
 * PaymentWithdrawalService interface for managing operations related to payment withdrawals.
 */
public interface PaymentWithdrawalService {
  PaymentWithdrawalResponse createPaymentWithdrawal(Long paymentId);
  void deletePaymentWithdrawal(Long paymentWithdrawalId);
  List<PaymentWithdrawalResponse> getPaymentWithdrawalsByPaymentId(Long paymentId);
  boolean isPaymentWithdrawalNeeded(Long paymentId);
}
