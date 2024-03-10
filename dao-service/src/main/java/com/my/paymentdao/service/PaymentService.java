package com.my.paymentdao.service;

import com.my.paymentdao.dto.payment.PaymentBriefResponse;
import com.my.paymentdao.dto.payment.PaymentClientBriefResponse;
import com.my.paymentdao.dto.payment.PaymentRequest;
import com.my.paymentdao.dto.payment.PaymentResponse;
import com.my.paymentdao.persistence.domain.entity.Payment;
import java.util.List;

public interface PaymentService {
  PaymentResponse createPayment(PaymentRequest paymentRequest);

  PaymentResponse updatePayment(Long id, PaymentRequest paymentRequest);

  PaymentResponse getPaymentById(Long id);

  List<PaymentBriefResponse> getPayments();

  List<PaymentClientBriefResponse> getPaymentsByInn(String inn);

  List<PaymentClientBriefResponse> getPaymentsByOkpo(String okpo);

  void deletePaymentById(Long id);

  Payment getPaymentEntityById(Long id);
}
