package com.my.paymentdao.mapper;

import com.my.paymentdao.dto.payment.PaymentClientBriefResponse;
import com.my.paymentdao.dto.payment.PaymentRequest;
import com.my.paymentdao.dto.payment.PaymentResponse;
import com.my.paymentdao.persistence.domain.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {
  Payment mapRequestToEntity(PaymentRequest paymentRequest);

  PaymentResponse mapEntityToResponse(Payment payment);

  PaymentClientBriefResponse mapEntityToBriefResponse(Payment payment);

  void updateEntityWithRequest(@MappingTarget Payment payment, PaymentRequest paymentRequest);
}
