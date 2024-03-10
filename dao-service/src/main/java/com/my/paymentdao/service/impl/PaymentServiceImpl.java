package com.my.paymentdao.service.impl;

import com.my.paymentdao.dto.payment.PaymentBriefResponse;
import com.my.paymentdao.dto.payment.PaymentClientBriefResponse;
import com.my.paymentdao.dto.payment.PaymentRequest;
import com.my.paymentdao.dto.payment.PaymentResponse;
import com.my.paymentdao.mapper.PaymentMapper;
import com.my.paymentdao.persistence.domain.entity.Payment;
import com.my.paymentdao.persistence.repository.PaymentRepository;
import com.my.paymentdao.service.PaymentService;
import com.my.paymentdao.service.exception.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
  private final PaymentRepository paymentRepository;
  private final PaymentMapper paymentMapper;

  @Override
  public PaymentResponse createPayment(PaymentRequest paymentRequest) {
    Payment payment = paymentMapper.mapRequestToEntity(paymentRequest);
    payment.setPayerInn(paymentRequest.payerInn());

    payment = paymentRepository.save(payment);
    return paymentMapper.mapEntityToResponse(payment);
  }

  @Transactional
  @Override
  public PaymentResponse updatePayment(Long id, PaymentRequest paymentRequest) {
    Payment payment = getPaymentEntityById(id);
    paymentMapper.updateEntityWithRequest(payment, paymentRequest);
    return paymentMapper.mapEntityToResponse(payment);
  }

  @Override
  public PaymentResponse getPaymentById(Long id) {
    return paymentRepository.findByIdAsPaymentResponse(id)
        .orElseThrow(() -> new EntityNotFoundException("Payment with id=%d was not found"
            .formatted(id)));
  }

  @Override
  public List<PaymentBriefResponse> getPayments() {
    return paymentRepository.findAllWithLastWithdrawalDateTime();
  }

  @Override
  public List<PaymentClientBriefResponse> getPaymentsByInn(String inn) {
    return paymentRepository.findAllByPayerInn(inn).stream()
        .map(paymentMapper::mapEntityToBriefResponse)
        .toList();
  }

  public List<PaymentClientBriefResponse> getPaymentsByOkpo(String okpo) {
    return paymentRepository.findAllByRecipientOkpo(okpo).stream()
        .map(paymentMapper::mapEntityToBriefResponse)
        .toList();
  }

  public void deletePaymentById(Long id) {
    Payment payment = getPaymentEntityById(id);
    paymentRepository.delete(payment);
  }

  public Payment getPaymentEntityById(Long id) {
    return paymentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Payment with id=%d was not found"
            .formatted(id)));
  }
}
