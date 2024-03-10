package com.my.paymentbusinesslogic.service.impl;

import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentBriefResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentClientBriefResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentResponse;
import com.my.paymentbusinesslogic.dto.payment.PaymentRequest;
import com.my.paymentbusinesslogic.service.PaymentService;
import com.my.paymentbusinesslogic.service.client.PaymentDaoClient;
import com.my.paymentbusinesslogic.util.EncryptionUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link PaymentService} interface for handling payment-related operations.
 */
@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
  private final PaymentDaoClient paymentDaoClient;
  private final EncryptionUtil encryptionUtil;

  @Override
  public PaymentResponse createPayment(PaymentRequest paymentRequest) {
    encryptPaymentRequestFields(paymentRequest);
    PaymentResponse paymentResponse = paymentDaoClient.createPayment(paymentRequest).getBody();
    decryptPaymentResponseFields(paymentResponse);

    return paymentResponse;
  }

  @Override
  public List<PaymentBriefResponse> getPayments() {
    return paymentDaoClient.getPayments().getBody();
  }

  @Override
  public List<PaymentClientBriefResponse> getPaymentsByInn(String inn) {
    return paymentDaoClient.getPayerPaymentsByInn(inn).getBody();
  }

  @Override
  public List<PaymentClientBriefResponse> getPaymentsByOkpo(String okpo) {
    return paymentDaoClient.getRecipientPaymentsByOkpo(okpo).getBody();
  }

  private void encryptPaymentRequestFields(PaymentRequest paymentRequest) {
    String encryptedPayerCardNumber = encryptionUtil.encrypt(paymentRequest.getPayerCardNumber());
    paymentRequest.setPayerCardNumber(encryptedPayerCardNumber);

    String encryptedPayerInn = encryptionUtil.encrypt(paymentRequest.getPayerInn());
    paymentRequest.setPayerInn(encryptedPayerInn);
  }

  private void decryptPaymentResponseFields(PaymentResponse paymentResponse) {
    String decryptedPayerCardNumber = encryptionUtil.decrypt(paymentResponse.getPayerCardNumber());
    paymentResponse.setPayerCardNumber(decryptedPayerCardNumber);

    String decryptedPayerInn = encryptionUtil.decrypt(paymentResponse.getPayerInn());
    paymentResponse.setPayerInn(decryptedPayerInn);
  }
}
