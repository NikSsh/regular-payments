package com.my.paymentdao.service.impl;

import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import com.my.paymentdao.mapper.PaymentWithdrawalMapper;
import com.my.paymentdao.persistence.domain.entity.Payment;
import com.my.paymentdao.persistence.domain.entity.PaymentWithdrawal;
import com.my.paymentdao.persistence.domain.enums.PaymentWithdrawalStatus;
import com.my.paymentdao.persistence.repository.PaymentWithdrawalRepository;
import com.my.paymentdao.service.PaymentService;
import com.my.paymentdao.service.PaymentWithdrawalService;
import com.my.paymentdao.service.exception.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PaymentWithdrawalServiceImpl implements PaymentWithdrawalService {
  private final PaymentService paymentService;
  private final PaymentWithdrawalRepository paymentWithdrawalRepository;
  private final PaymentWithdrawalMapper paymentWithdrawalMapper;

  public PaymentWithdrawalResponse getById(Long id) {
    PaymentWithdrawal paymentWithdrawal = getPaymentWithdrawalEntityById(id);
    return paymentWithdrawalMapper.mapEntityToResponse(paymentWithdrawal);
  }


  @Transactional
  public void changePaymentWithdrawalStatusToStornowed(Long id) {
    PaymentWithdrawal paymentWithdrawal = getPaymentWithdrawalEntityById(id);
    paymentWithdrawal.setStatus(PaymentWithdrawalStatus.S);
  }

  public List<PaymentWithdrawalResponse> getAllByPaymentId(Long paymentId) {
    return this.paymentWithdrawalRepository.findAllByPaymentId(paymentId);
  }

  public PaymentWithdrawalResponse createPaymentWithdrawal(Long paymentId) {
    Payment payment = paymentService.getPaymentEntityById(paymentId);
    PaymentWithdrawal paymentWithdrawal = new PaymentWithdrawal();
    paymentWithdrawal.setAmount(payment.getAmount());
    paymentWithdrawal.setStatus(PaymentWithdrawalStatus.A);
    paymentWithdrawal.setPayment(payment);
    paymentWithdrawal = paymentWithdrawalRepository.save(paymentWithdrawal);
    return paymentWithdrawalMapper.mapEntityToResponse(paymentWithdrawal);
  }

  private PaymentWithdrawal getPaymentWithdrawalEntityById(Long id) {
    return paymentWithdrawalRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Payment withdrawal with id=%d was not found"
            .formatted(id)));
  }
}
