package com.my.paymentbusinesslogic.service;

import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentBriefResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentClientBriefResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentResponse;
import com.my.paymentbusinesslogic.dto.payment.PaymentRequest;
import java.util.List;

/**
 * PaymentService interface for managing operations related to payments.
 */
public interface PaymentService {

  PaymentResponse createPayment(PaymentRequest paymentRequest);
  List<PaymentBriefResponse> getPayments();
  List<PaymentClientBriefResponse> getPaymentsByInn(String inn);
  List<PaymentClientBriefResponse> getPaymentsByOkpo(String okpo);
}
