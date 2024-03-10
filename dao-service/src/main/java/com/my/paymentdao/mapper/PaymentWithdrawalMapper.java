package com.my.paymentdao.mapper;

import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import com.my.paymentdao.persistence.domain.entity.PaymentWithdrawal;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentWithdrawalMapper {
  PaymentWithdrawalResponse mapEntityToResponse(PaymentWithdrawal paymentWithdrawal);
}
